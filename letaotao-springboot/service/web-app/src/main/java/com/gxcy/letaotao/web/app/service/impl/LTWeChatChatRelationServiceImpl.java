package com.gxcy.letaotao.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.web.app.entity.LTChatRelation;
import com.gxcy.letaotao.web.app.mapper.LTChatRelationMapper;
import com.gxcy.letaotao.web.app.mapper.LTMessageMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatChatRelationService;
import com.gxcy.letaotao.web.app.vo.LTChatRelationVo;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 聊天关系表 服务实现类
 */
@Service
@Slf4j
public class LTWeChatChatRelationServiceImpl extends ServiceImpl<LTChatRelationMapper, LTChatRelation> implements LTWeChatChatRelationService {

    @Resource
    private CacheManager cacheManager;
    @Resource
    private LTMessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTChatRelationVo getOrCreateChatRelation(Long buyerId, Long sellerId, Integer productId) {
        // 先查询聊天关系是否存在 如果不存在就创建
        LTChatRelationVo chatRelation = baseMapper.findRelation(buyerId, sellerId, productId);
        if (chatRelation == null) {
            LTChatRelationVo relationVo = new LTChatRelationVo();
            relationVo.setBuyerId(buyerId);
            relationVo.setSellerId(sellerId);
            relationVo.setProductId(productId);
            // 保存
            this.add(relationVo);
        }
        return chatRelation;
    }

    @Override
    public List<LTWechatMessageVo> getChatMessages(Integer relationId) {
        // 查询聊天记录
        return baseMapper.selectMessagesByRelationId(relationId);
    }

    @Override
    public List<LTChatRelationVo> findRelationListByUserId(Long userId) {
        return baseMapper.findRelationListByUserId(userId);
    }

    @Override
    public int selectRelationIdByProductId(Integer productId) {
        Integer id = baseMapper.selectRelationIdByProductId(productId);
        return id == null ? 0 : id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTChatRelationVo chatRelationVo) {
        LTChatRelation chatRelation = this.convert(chatRelationVo);
        if (this.save(chatRelation)) {
            this.cacheChatRelationById(chatRelation.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.CHAT_RELATION, key = "#id")
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCatchById(Integer id) {
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.CHAT_RELATION)).evict(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProductId(Integer productId) {
        // 获取关联关系ID
        int relationId = this.selectRelationIdByProductId(productId);
        if (relationId > 0) {
            // 删除关联关系
            messageMapper.deleteByRelationId(relationId);
        }
        this.deleteCatchById(relationId);
    }

    @CachePut(value = CacheKeyConstants.CHAT_RELATION, key = "#result.id", unless = "#result == null")
    public LTChatRelationVo cacheChatRelationById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTChatRelation convert(LTChatRelationVo chatRelationVo) {
        LTChatRelation chatRelation = new LTChatRelation();
        BeanUtils.copyProperties(chatRelationVo, chatRelation);
        return chatRelation;
    }

    private LTChatRelationVo convert(LTChatRelation chatRelation) {
        LTChatRelationVo chatRelationVo = new LTChatRelationVo();
        BeanUtils.copyProperties(chatRelation, chatRelationVo);
        return chatRelationVo;
    }
}
