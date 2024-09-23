package com.gxcy.letaotao.web.app.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTPost;
import com.gxcy.letaotao.common.enums.*;
import com.gxcy.letaotao.common.exception.LTException;
import com.gxcy.letaotao.web.app.dto.LTWeChatPostDTO;
import com.gxcy.letaotao.web.app.mapper.LTPostMapper;
import com.gxcy.letaotao.web.app.service.*;
import com.gxcy.letaotao.web.app.vo.LTImagesVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LTWechatPostQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatPostVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LTWeChatPostServiceImpl extends BaseServiceImpl<LTPostMapper, LTPost> implements LTWeChatPostService {

    @Resource
    private LTImagesService ltImagesService;
    @Resource
    private LTWeChatCollectionService ltCollectionService;
    @Resource
    private LTWeChatLikeService ltLikeService;
    @Resource
    private LTWeChatCommentService ltCommentService;
    @Resource
    private LTWeChatUserFollowService ltUserFollowService;
    @Resource
    private WeChatUserService userService;

    @Override
    public IPage<LTWechatPostVo> findListByPage(IPage<LTWechatPostVo> page, LTWechatPostQueryVo postVO) {
        // 创建条件构造器对象
        QueryWrapper<LTWechatPostQueryVo> queryWrapper = new QueryWrapper<>();

        // 标题
        queryWrapper.like(!ObjectUtils.isEmpty(postVO.getTitle()),
                "p.title", postVO.getTitle());
        // 帖子内容
        queryWrapper.like(!ObjectUtils.isEmpty(postVO.getContent()),
                "p.content", postVO.getContent());

        // 发布者
        queryWrapper.eq(!ObjectUtils.isEmpty(postVO.getUserId()) && postVO.getUserId() > 0,
                "p.user_id", postVO.getUserId());

        // 查询当前用户关注的用户的帖子
        if (!ObjectUtils.isEmpty(postVO.getFollowerId()) && postVO.getFollowerId() > 0) {
            List<Long> followedIds = ltUserFollowService.findFollowingIdsList(postVO.getFollowerId());
            if (followedIds.isEmpty()) {
                // 返回一个空的结果集
                queryWrapper.eq("p.user_id", -1);
            } else {
                queryWrapper.in("p.user_id", followedIds);
            }
        }

        // 帖子状态
        queryWrapper.eq(!ObjectUtils.isEmpty(postVO.getStatus()),
                "p.status", postVO.getStatus());
        if (ObjectUtils.isEmpty(postVO.getStatus())) {
            queryWrapper.notIn("p.status", LTPostStatus.STATUS_DRAFT, LTPostStatus.STATUS_PENDING_REVIEW);
        }
        if (!ObjectUtils.isEmpty(postVO.getLabel())) {
            String label = postVO.getLabel();
            queryWrapper.and(qw -> {
                qw.like("p.title", label) // 标题
                        .or(i -> i.like("p.content", label)); // 内容
            });
        }
        queryWrapper.orderByDesc("p.post_time");
        IPage<LTWechatPostVo> list = baseMapper.findListByPage(page, queryWrapper); // 查询帖子列表
        List<LTWechatPostVo> ltPosts = list.getRecords();
        for (LTWechatPostVo ltPost : ltPosts) {
            // 查询和帖子关联的图片
            ltPost.setImages(ltImagesService.getImagesList(ltPost.getId(), LTImagesType.POST));
            // 获取帖子收藏、点赞、评论数量
            ltPost.setCollectCount(ltCollectionService.findCollectionCount(ltPost.getId(), LTCollectionTargetType.POST));
            ltPost.setLikeCount(ltLikeService.findLikeCount(ltPost.getId(), LTLikeTargetType.POST));
            ltPost.setCommentCount(ltCommentService.findCommentCount(ltPost.getId(), LTCommentType.COMMENT));
            // 获取用户粉丝数
            ltPost.setFollowerCount(ltUserFollowService.findFollowerCount(ltPost.getUserId()));
            // 获取帖子发布者是否被当前用户关注
            ltPost.setIsFollowed(ltUserFollowService.isFollow(postVO.getCurrentUserId(), ltPost.getUserId()) ? BooleanStatus.TRUE : BooleanStatus.FALSE);

            boolean isLiked = ltLikeService.isLike(ltPost.getId(), LTLikeTargetType.POST, postVO.getCurrentUserId()); // 判断当前用户是否点赞
            ltPost.setIsLiked(isLiked ? BooleanStatus.TRUE : BooleanStatus.FALSE);
        }
        // 查询并返回数据
        return list;
    }

    @Override
    public IPage<LTWechatPostVo> findListByUserId(IPage<LTWechatPostVo> page, LTWechatPostQueryVo postQueryVo) {
        return this.findListByPage(page, postQueryVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int publish(LTWeChatPostDTO postDTO) {
        return this.handlePostOperation(postDTO, LTPostStatus.STATUS_PUBLISHED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(LTWeChatPostDTO postDTO) {
        return this.handlePostOperation(postDTO, LTPostStatus.STATUS_DRAFT);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.POST, key = "#id", unless = "#result == null")
    public LTWechatPostVo findPostById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTWechatPostVo postVo) {
        if (this.save(this.convert(postVo))) {
            this.cachePostById(postVo.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTWeChatPostDTO postDTO) {
        if (this.save(this.convert(postDTO))) {
            this.cachePostById(postDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.POST, key = "#postVo.id")
    public boolean update(LTWechatPostVo postVo) {
        return this.updateById(this.convert(postVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.POST, key = "#postDTO.id")
    public boolean update(LTWeChatPostDTO postDTO) {
        List<LTImagesVo> images = postDTO.getNewImages();
        if (images != null && !images.isEmpty()) {
            // 更新图片和商品关联
            for (LTImagesVo image : images) {
                image.setRelatedId(postDTO.getId());
            }
            ltImagesService.batchUpdate(images);
        }
        return this.updateById(this.convert(postDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.POST, key = "#id")
    public boolean deleteById(Integer id) {
        if (this.removeById(id)) {
            // 从数据库中查询帖子关联的所有图片
            List<LTImagesVo> ltImages = ltImagesService.getImagesList(id, LTImagesType.POST);
            ltImagesService.batchDelete(ltImages);
            // 删除帖子关联的收藏、点赞、评论
            ltCollectionService.deleteByTargetIdAndType(id, LTCollectionTargetType.POST);
            ltLikeService.deleteByTargetId(id, LTLikeTargetType.POST);
            ltCommentService.deleteByReferenceIdAndType(id, LTCommentType.COMMENT);

            log.info("帖子删除成功，帖子编号：{}", id);
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.POST, key = "#id", unless = "#result == null")
    public LTWechatPostVo findRelation(Long id, Long userId) {
        // 通过id查询帖子
        LTWechatPostVo postVo = this.convert(this.getById(id));
        // 获取用户信息
        LTUserVo user = userService.findUserById(postVo.getUserId());
        postVo.setAvatar(user.getAvatar());
        postVo.setNickName(user.getNickName());

        // 获取帖子关联的所有图片
        postVo.setImages(ltImagesService.getImagesList(postVo.getId(), LTImagesType.POST));
        // 获取帖子收藏、点赞、评论数量
        postVo.setCollectCount(ltCollectionService.findCollectionCount(postVo.getId(), LTCollectionTargetType.POST));
        postVo.setLikeCount(ltLikeService.findLikeCount(postVo.getId(), LTLikeTargetType.POST));
        postVo.setCommentCount(ltCommentService.findCommentCount(postVo.getId(), LTCommentType.COMMENT));

        if (userId != null && userId > 0) {
            boolean isLiked = ltLikeService.isLike(postVo.getId(), LTLikeTargetType.POST, userId);
            postVo.setIsLiked(isLiked ? BooleanStatus.TRUE : BooleanStatus.FALSE); // 是否点赞

            boolean isCollected = ltCollectionService.isCollection(postVo.getId(), LTCollectionTargetType.POST, userId);
            boolean isFollowed = ltUserFollowService.isFollow(userId, postVo.getUserId());
            postVo.setIsFavorite(isCollected ? BooleanStatus.TRUE : BooleanStatus.FALSE); // 是否收藏
            postVo.setIsFollowed(isFollowed ? BooleanStatus.TRUE : BooleanStatus.FALSE); // 是否关注
        }
        return postVo;
    }

    @CachePut(value = CacheKeyConstants.POST, key = "#result.id", unless = "#result == null")
    public LTWechatPostVo cachePostById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTWechatPostVo convert(LTPost ltPost) {
        LTWechatPostVo ltPostVo = new LTWechatPostVo();
        BeanUtils.copyProperties(ltPost, ltPostVo);
        return ltPostVo;
    }

    private LTPost convert(LTWechatPostVo postVo) {
        LTPost ltPost = new LTPost();
        BeanUtils.copyProperties(postVo, ltPost);
        return ltPost;
    }

    private LTPost convert(LTWeChatPostDTO postDTO) {
        LTPost ltPost = new LTPost();
        BeanUtils.copyProperties(postDTO, ltPost);
        return ltPost;
    }

    @Transactional(rollbackFor = Exception.class)
    protected int handlePostOperation(LTWeChatPostDTO postDTO, LTPostStatus status) {
        String desc = status.equals(LTPostStatus.STATUS_PUBLISHED) ? "发布" : "存草稿";
        try {
            postDTO.setStatus(status); // 设置状态
            postDTO.setPostTime(LocalDateTime.now());

            LTPost ltPost = this.convert(postDTO);
            boolean insert = this.save(ltPost);
            if (insert) {
                log.info("用户编号：{} 帖子{}成功，帖子编号：{}",
                        postDTO.getUserId(), desc, ltPost.getId());
                List<LTImagesVo> images = postDTO.getNewImages();
                if (images != null && !images.isEmpty()) {
                    // 更新图片和帖子关联
                    images.forEach(image -> image.setRelatedId(ltPost.getId()));
                    ltImagesService.batchUpdate(images);
                }
            }

            return insert ? ltPost.getId() : 0;
        } catch (IllegalArgumentException e) {
            log.error(String.format("帖子%s失败，参数校验不通过", desc), e);
            throw new LTException(String.format("帖子%s失败，参数校验不通过", desc), e);
        }
    }

}
