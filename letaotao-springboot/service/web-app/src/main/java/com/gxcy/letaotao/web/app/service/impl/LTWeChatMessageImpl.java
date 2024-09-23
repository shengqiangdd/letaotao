package com.gxcy.letaotao.web.app.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTMessage;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.web.app.entity.LTUser;
import com.gxcy.letaotao.web.app.mapper.LTMessageMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatChatRelationService;
import com.gxcy.letaotao.web.app.service.LTWeChatMessageService;
import com.gxcy.letaotao.web.app.service.LTWeChatProductService;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.vo.LTChatRelationVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;
import com.gxcy.letaotao.web.app.vo.LTWechatProductVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class LTWeChatMessageImpl extends BaseServiceImpl<LTMessageMapper, LTMessage> implements LTWeChatMessageService {

    @Resource
    private LTWeChatChatRelationService chatRelationService;
    @Resource
    private WeChatUserService userService;
    @Resource
    private LTWeChatProductService productService;
    @Resource
    private CacheManager cacheManager;

    @Override
    public List<LTChatRelationVo> getChatMessageList() {
        LambdaQueryWrapper<LTMessage> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<LTUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LTUserVo currentUser = userService.getCurrentUser();
        // 获取当前用户的聊天列表
        List<LTChatRelationVo> chatRelations = chatRelationService.findRelationListByUserId(currentUser.getId());
        for (LTChatRelationVo chatRelation : chatRelations) {
            messageLambdaQueryWrapper.clear();
            messageLambdaQueryWrapper.eq(LTMessage::getRelationId, chatRelation.getId());
            if (chatRelation.getBuyerId().equals(currentUser.getId())) { // 如果当前用户是买家
                messageLambdaQueryWrapper.eq(LTMessage::getSenderId, chatRelation.getSellerId());
                Long count = baseMapper.selectCount(messageLambdaQueryWrapper); // 查询卖家发送的最后一条消息
                if (count <= 0) {
                    messageLambdaQueryWrapper.clear();
                    messageLambdaQueryWrapper.eq(LTMessage::getRelationId, chatRelation.getId())
                            .eq(LTMessage::getSenderId, chatRelation.getBuyerId()); // 查询买家发送的最后一条消息
                }
                userLambdaQueryWrapper.clear();
                userLambdaQueryWrapper.eq(LTUser::getId, chatRelation.getSellerId());
            } else { // 如果当前用户是卖家
                messageLambdaQueryWrapper.eq(LTMessage::getSenderId, chatRelation.getBuyerId()); // 查询买家发送的最后一条消息
                userLambdaQueryWrapper.clear();
                userLambdaQueryWrapper.eq(LTUser::getId, chatRelation.getBuyerId());
            }
            messageLambdaQueryWrapper.orderByDesc(LTMessage::getSendTime);
            messageLambdaQueryWrapper.last("LIMIT 1"); // 只返回一条数据
            List<LTMessage> ltMessages = baseMapper.selectList(messageLambdaQueryWrapper); // 获取最后一条消息
            if (!ltMessages.isEmpty()) {
                LTMessage message = ltMessages.get(0);
                LTWechatMessageVo messageVo = new LTWechatMessageVo();
                BeanUtils.copyProperties(message, messageVo);
                LTUser user = userService.getOne(userLambdaQueryWrapper);
                messageVo.setNickName(user.getNickName());
                messageVo.setAvatar(user.getAvatar());
                chatRelation.setMessage(messageVo);

                LTUserVo seller = userService.findUserById(chatRelation.getSellerId()); // 卖家信息
                LTUserVo buyer = userService.findUserById(chatRelation.getBuyerId()); // 买家信息

                chatRelation.setBuyer(buyer);
                chatRelation.setSeller(seller);

                LTWechatProductVo product = productService.getProductRelationImagesById(chatRelation.getProductId()); // 商品信息
                chatRelation.setProduct(product);
            }
        }

        return chatRelations;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRead(List<Integer> ids) {
        LambdaUpdateWrapper<LTMessage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(LTMessage::getId, ids).set(LTMessage::getIsRead, BooleanStatus.TRUE);
        // 批量已读
        int update = baseMapper.update(null, updateWrapper);
        if (update > 0) {
            ids.forEach(id -> Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.MESSAGE)).evict(id));
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.MESSAGE, key = "#id")
    public LTWechatMessageVo findMessageById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTWechatMessageVo add(LTWechatMessageVo messageVo) {
        LTMessage message = this.convert(messageVo);
        if (this.save(message)) {
            this.cacheMessageById(message.getId());
            return this.convert(message);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.MESSAGE, key = "#messageVo.id")
    public boolean update(LTWechatMessageVo messageVo) {
        LTMessage message = this.convert(messageVo);
        return this.updateById(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.MESSAGE, key = "#id")
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @CachePut(value = CacheKeyConstants.MESSAGE, key = "#result.id")
    public LTWechatMessageVo cacheMessageById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTMessage convert(LTWechatMessageVo messageVo) {
        LTMessage message = new LTMessage();
        BeanUtils.copyProperties(messageVo, message);
        return message;
    }

    private LTWechatMessageVo convert(LTMessage message) {
        LTWechatMessageVo messageVo = new LTWechatMessageVo();
        BeanUtils.copyProperties(message, messageVo);
        return messageVo;
    }
}
