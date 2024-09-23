package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTPost;
import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import com.gxcy.letaotao.web.admin.mapper.*;
import com.gxcy.letaotao.web.admin.service.LTPostService;
import com.gxcy.letaotao.web.admin.vo.LTImagesVo;
import com.gxcy.letaotao.web.admin.vo.LTPostQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTPostVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 帖子表 服务实现类
 */
@Service
@Slf4j
public class LTPostServiceImpl extends ServiceImpl<LTPostMapper, LTPost> implements LTPostService {

    @Resource
    private LTImagesMapper ltImagesMapper;
    @Resource
    private LTCollectionMapper ltCollectionMapper;
    @Resource
    private LTCommentMapper ltCommentMapper;
    @Resource
    private LTLikeMapper ltLikeMapper;
    @Resource
    private CacheManager cacheManager;

    @Override
    @Cacheable(value = CacheKeyConstants.POST, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<LTPostVo> findListByPage(IPage<LTPostVo> page, LTPostQueryVo postQueryVo) {
        // 创建条件构造器对象
        QueryWrapper<LTPostQueryVo> queryWrapper = new QueryWrapper<>();

        // 用户昵称
        queryWrapper.likeRight(!ObjectUtils.isEmpty(postQueryVo.getNickName()),
                "u.nick_name", postQueryVo.getNickName());
        // 标题
        queryWrapper.likeRight(!ObjectUtils.isEmpty(postQueryVo.getTitle()),
                "p.title", postQueryVo.getTitle());
        // 帖子内容
        queryWrapper.likeRight(!ObjectUtils.isEmpty(postQueryVo.getContent()),
                "p.content", postQueryVo.getContent());

        // 发帖时间
        if (!ObjectUtils.isEmpty(postQueryVo.getBetweenTime())) {
            String[] betweenTime = postQueryVo.getBetweenTime();
            Timestamp startTime = Timestamp.valueOf(betweenTime[0]);
            Timestamp endTime = Timestamp.valueOf(betweenTime[1]);
            queryWrapper.between("p.post_time", startTime, endTime);
        }
        // 帖子状态
        queryWrapper.eq(!ObjectUtils.isEmpty(postQueryVo.getStatus()),
                "p.status", postQueryVo.getStatus());

        IPage<LTPostVo> list = baseMapper.findListByPage(page, queryWrapper); // 分页查询
        List<LTPostVo> ltPosts = list.getRecords();
        for (LTPostVo ltPost : ltPosts) {
            // 查询和帖子关联的图片
            List<LTImagesVo> images = ltImagesMapper.getImagesList(ltPost.getId(), LTImagesType.POST);
            ltPost.setImages(images);
            // 获取帖子收藏、点赞、评论数量
            ltPost.setCollectCount(ltCollectionMapper.findCollectionCount(ltPost.getId(), LTCollectionTargetType.POST));
            ltPost.setLikeCount(ltLikeMapper.findLikeCount(ltPost.getId(), LTLikeTargetType.POST));
            ltPost.setCommentCount(ltCommentMapper.findCommentCount(ltPost.getId(), LTCommentType.COMMENT));
        }

        // 查询并返回数据
        return list;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.POST, key = "#id", unless = "#result == null")
    public LTPostVo findPostById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTPostVo postVo) {
        if (this.save(this.convert(postVo))) {
            this.cachePostById(postVo.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.POST, key = "#postVo.id")
    public boolean update(LTPostVo postVo) {
        return this.updateById(this.convert(postVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<LTPostVo> ltPostVos) {
        if (this.updateBatchById(this.convert(ltPostVos))) {
            ltPostVos.forEach(ltPostVo -> {
                Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.POST)).evictIfPresent(ltPostVo.getId());
            });
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.POST, key = "#id")
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @CachePut(value = CacheKeyConstants.POST, key = "#result.id", unless = "#result == null")
    public LTPostVo cachePostById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTPostVo convert(LTPost ltPost) {
        LTPostVo ltPostVo = new LTPostVo();
        BeanUtils.copyProperties(ltPost, ltPostVo);
        return ltPostVo;
    }

    private LTPost convert(LTPostVo postVo) {
        LTPost ltPost = new LTPost();
        BeanUtils.copyProperties(postVo, ltPost);
        return ltPost;
    }

    private List<LTPost> convert(List<LTPostVo> postVos) {
        return postVos.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

}
