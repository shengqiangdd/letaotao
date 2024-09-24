package com.gxcy.letaotao.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTComment;
import com.gxcy.letaotao.common.entity.LTLike;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import com.gxcy.letaotao.web.app.mapper.LTCommentMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatCommentService;
import com.gxcy.letaotao.web.app.service.LTWeChatLikeService;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 留言/评论表 服务实现类
 */
@Service
public class LTWeChatCommentServiceImpl extends ServiceImpl<LTCommentMapper, LTComment> implements LTWeChatCommentService {

    @Resource
    private LTWeChatLikeService ltLikeService;
    @Resource
    private WeChatUserService userService;
    @Resource
    private CacheManager cacheManager;

    @Override
    public List<LTWechatCommentVo> findList(LTWechatCommentQueryVo commentVO) {
        return baseMapper.findList(commentVO);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.COMMENT, key = "#commentVO.referenceId+'_'+#commentVO.type.code", unless = "#result == null")
    public List<LTWechatCommentVo> getCommentTree(LTWechatCommentQueryVo commentVO) {
        // 查询所有留言/评论
        List<LTWechatCommentVo> comments = findList(commentVO);
        if (commentVO.getType() == LTCommentType.LEAVE_MESSAGE) {
            Stream<LTWechatCommentVo> commentStream = comments.stream()
                    .filter(comment -> Objects.equals(comment.getUserId(), commentVO.getPublisherId()));
            commentStream.forEach(comment -> comment.setIsSeller(true));
        }

        // 构建树形结构，首先找到所有顶级评论, 即parentId为0的评论
        List<LTWechatCommentVo> tree = comments.stream()
                .filter(comment -> comment.getParentId() == 0)
                .collect(Collectors.toList());

        // 构建父子关系
        Map<Integer, List<LTWechatCommentVo>> childrenMap = comments.stream()
                .filter(comment -> comment.getParentId() != 0)
                .collect(Collectors.groupingBy(LTWechatCommentVo::getParentId));

        for (LTWechatCommentVo treeNode : tree) {
            addChildren(treeNode, childrenMap);
        }

        // 返回树形结构
        return tree;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.COMMENT, key = "'count_'+#referenceId+'_'+#type.code", unless = "#result == null")
    public int findCommentCount(Integer referenceId, LTCommentType type) {
        // 查询留言/评论总数
        return baseMapper.findCommentCount(referenceId, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByReferenceIdAndType(Integer referenceId, LTCommentType type) {
        if (type == LTCommentType.COMMENT) {
            // 删除点赞记录
            ltLikeService.deleteByTargetId(referenceId, LTLikeTargetType.COMMENT);
        }
        baseMapper.deleteByReferenceId(referenceId, type); // 删除留言/评论
        LTWechatCommentQueryVo commentQueryVo = new LTWechatCommentQueryVo();
        commentQueryVo.setReferenceId(referenceId);
        commentQueryVo.setType(type);
        List<LTWechatCommentVo> commentVoList = this.findList(commentQueryVo);
        commentVoList.forEach(commentVo -> { // 删除缓存
            Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.COMMENT)).evictIfPresent(commentVo.getId());
            this.deleteCacheByReferenceIdAndType(referenceId, type);
        });
    }

    @Override
    @Cacheable(value = CacheKeyConstants.COMMENT, key = "#result.id", unless = "#result == null")
    public LTWechatCommentVo findCommentById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTWechatCommentVo add(LTWechatCommentVo commentVo) {
        if (commentVo.getContent().isEmpty()) {
            return null;
        }
        commentVo.setCommentTime(LocalDateTime.now());
        LTComment comment = this.convert(commentVo);
        if (this.save(comment)) {
            this.cacheCommentById(comment.getId());
            this.deleteCacheByReferenceIdAndType(comment.getReferenceId(), comment.getType()); // 删除缓存
            LTUserVo userVo = userService.findUserById(comment.getUserId());
            commentVo.setId(comment.getId());
            commentVo.setNickName(userVo.getNickName());
            commentVo.setAvatar(userVo.getAvatar());
            return commentVo;
        }
        return null;
    }

    @Override
    @CacheEvict(value = CacheKeyConstants.COMMENT, key = "#commentVo.id")
    @Transactional(rollbackFor = Exception.class)
    public boolean update(LTWechatCommentVo commentVo) {
        LTComment comment = this.convert(commentVo);
        boolean updated = this.updateById(comment);
        if (updated) {
            this.deleteCacheByReferenceIdAndType(comment.getReferenceId(), comment.getType()); // 删除缓存
        }
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer id) {
        boolean delete = this.removeById(id);
        if (delete) {
            LTComment ltComment = this.getById(id);
            this.deleteByReferenceIdAndType(ltComment.getReferenceId(), ltComment.getType());
        }
        return delete;
    }

    @CachePut(value = CacheKeyConstants.COMMENT, key = "#result.id", unless = "#result == null")
    public LTWechatCommentVo cacheCommentById(Integer id) {
        return this.convert(this.getById(id));
    }

    private void deleteCacheByReferenceIdAndType(Integer referenceId, LTCommentType type) {
        Cache cache = cacheManager.getCache(CacheKeyConstants.COMMENT);
        if (cache != null) {
            String countCacheKey = "count_" + referenceId + "_" + type.getCode();
            String listCacheKey = referenceId + "_" + type.getCode();
            cache.evict(listCacheKey);
            cache.evict(countCacheKey);
        }
    }

    private LTComment convert(LTWechatCommentVo commentVo) {
        LTComment comment = new LTComment();
        BeanUtils.copyProperties(commentVo, comment);
        return comment;
    }

    private LTWechatCommentVo convert(LTComment comment) {
        LTWechatCommentVo commentVo = new LTWechatCommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        return commentVo;
    }

    private void addChildren(LTWechatCommentVo parent, Map<Integer, List<LTWechatCommentVo>> childrenMap) { // 递归添加子节点
        List<LTWechatCommentVo> children = childrenMap.get(parent.getId());
        if (children != null) {
            for (LTWechatCommentVo child : children) {
                addChildren(child, childrenMap);
            }

            parent.setChild(children);
        }
    }

    private void setCommentLikedStatus(List<LTWechatCommentVo> comments, Map<Integer, List<LTWechatCommentVo>> childrenMap, Long userId, LTLikeTargetType type) {
        // 批量查询获取当前用户点赞的所有留言/评论的ID列表
        List<Integer> likedCommentIds = ltLikeService.list(new LambdaQueryWrapper<LTLike>()
                        .eq(LTLike::getTargetType, type == LTLikeTargetType.POST ? LTLikeTargetType.LEAVE_MESSAGE : LTLikeTargetType.COMMENT)
                        .eq(LTLike::getIsActive, BooleanStatus.TRUE)
                        .eq(LTLike::getUserId, userId))
                .stream().map(LTLike::getTargetId).collect(Collectors.toList());

        // 递归设置点赞状态
        setCommentLikedStatusRecursively(comments, childrenMap, likedCommentIds);
    }


    private void setCommentLikedStatusRecursively(List<LTWechatCommentVo> comments, Map<Integer, List<LTWechatCommentVo>> childrenMap, List<Integer> likedCommentIds) {
        for (LTWechatCommentVo comment : comments) {
            // 直接检查留言/评论ID是否在已点赞的ID列表中
            boolean isLiked = likedCommentIds.contains(comment.getId());
            comment.setIsLiked(isLiked ? BooleanStatus.TRUE : BooleanStatus.FALSE);

            // 递归设置子留言/评论的点赞状态
            List<LTWechatCommentVo> children = childrenMap.getOrDefault(comment.getId(), new ArrayList<>());
            comment.setChild(children);
            setCommentLikedStatusRecursively(children, childrenMap, likedCommentIds);
        }
    }

}
