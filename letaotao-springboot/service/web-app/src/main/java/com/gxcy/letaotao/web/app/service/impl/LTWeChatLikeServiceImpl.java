package com.gxcy.letaotao.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTLike;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import com.gxcy.letaotao.web.app.dto.LTLikeDTO;
import com.gxcy.letaotao.web.app.mapper.LTLikeMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatLikeService;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 点赞表 服务实现类
 */
@Service
@Slf4j
public class LTWeChatLikeServiceImpl extends ServiceImpl<LTLikeMapper, LTLike> implements LTWeChatLikeService {

    @Resource
    private CacheManager cacheManager;
    @Resource
    private WeChatUserService wechatUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addOrUpdate(LTLikeDTO ltLike) {
        // 根据 targetId、userId、targetType 查询点赞记录
        LambdaQueryWrapper<LTLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LTLike::getTargetId, ltLike.getTargetId())
                .eq(LTLike::getUserId, ltLike.getUserId())
                .eq(LTLike::getTargetType, ltLike.getTargetType());

        LTLike existLike = baseMapper.selectOne(queryWrapper);

        BooleanStatus isActive;
        try {
            if (existLike != null) {
                // 如果点赞记录存在，则更新状态，并返回状态
                isActive = existLike.getIsActive() == BooleanStatus.TRUE ? BooleanStatus.FALSE : BooleanStatus.TRUE;
                existLike.setIsActive(isActive);
                baseMapper.updateById(existLike);
            } else {
                // 如果点赞记录不存在，则新增记录，并返回状态
                isActive = BooleanStatus.TRUE; // 新记录默认为激活状态
                LTLike like = new LTLike();
                BeanUtils.copyProperties(ltLike, like);
                like.setIsActive(isActive);
                baseMapper.insert(like);
            }
            this.deleteCache(ltLike.getTargetId(), ltLike.getTargetType());
            return isActive == BooleanStatus.TRUE ? BooleanStatus.TRUE.getCode() : BooleanStatus.FALSE.getCode();
        } catch (Exception e) {
            log.debug("点赞异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Cacheable(value = CacheKeyConstants.LIKE, key = "'count_'+#targetId + '_' + #targetType.code", unless = "#result == null")
    public int findLikeCount(Integer targetId, LTLikeTargetType targetType) {
        // 根据 targetId、targetType 查询点赞数量
        return baseMapper.findLikeCount(targetId, targetType);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.LIKE, key = "'bool_'+#userId+'_'+#targetId + '_' + #targetType.code", unless = "#result == null")
    public boolean isLike(Integer targetId, LTLikeTargetType targetType, Long userId) {
        // 根据 targetId、userId、targetType 查询点赞记录
        return baseMapper.isLike(targetId, targetType, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTargetId(Integer targetId, LTLikeTargetType targetType) {
        // 根据 targetId、targetType 删除点赞记录
        baseMapper.deleteByTargetId(targetId, targetType);
        this.deleteCache(targetId, targetType);
    }

    private void deleteCache(Integer targetId, LTLikeTargetType targetType) {
        Long userId = wechatUserService.getCurrentUser().getId();
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.LIKE))
                .evict("count_" +targetId + "_" + targetType.getCode());
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.LIKE))
                .evict("bool_" +userId +"_" +targetId + "_" + targetType.getCode());
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.POST)).evict(targetId); // 清除帖子缓存

        String commentCountCacheKey = "count_" + targetId + "_" + LTCommentType.COMMENT.getCode();
        String commentListCacheKey = targetId + "_" + LTCommentType.COMMENT.getCode();
        String leaveMessageCountCacheKey = "count_" + targetId + "_" + LTCommentType.LEAVE_MESSAGE.getCode();
        String leaveMessageListCacheKey = targetId + "_" + LTCommentType.LEAVE_MESSAGE.getCode();
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.COMMENT)).evict(commentCountCacheKey); // 清除评论数量缓存
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.COMMENT)).evict(commentListCacheKey); // 清除评论列表缓存
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.COMMENT)).evict(leaveMessageCountCacheKey); // 清除留言数量缓存
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.COMMENT)).evict(leaveMessageListCacheKey); // 清除留言列表缓存
    }
}
