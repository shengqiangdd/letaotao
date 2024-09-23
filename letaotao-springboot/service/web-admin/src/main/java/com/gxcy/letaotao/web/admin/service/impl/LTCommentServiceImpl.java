package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTComment;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.web.admin.mapper.LTCommentMapper;
import com.gxcy.letaotao.web.admin.service.LTCommentService;
import com.gxcy.letaotao.web.admin.vo.LTCommentQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTCommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 留言/评论表 服务实现类
 */
@Service
public class LTCommentServiceImpl extends ServiceImpl<LTCommentMapper, LTComment> implements LTCommentService {

    @Override
    @Cacheable(value = CacheKeyConstants.COMMENT, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<LTCommentVo> findListByPage(IPage<LTCommentVo> page, LTCommentQueryVo commentQueryVo) {
        // 创建条件构造器对象
        QueryWrapper<LTCommentQueryVo> queryWrapper = new QueryWrapper<>();

        // 留言/评论内容
        queryWrapper.likeRight(!ObjectUtils.isEmpty(commentQueryVo.getContent()),
                "c.content", commentQueryVo.getContent());
        // 留言/评论用户昵称
        queryWrapper.likeRight(!ObjectUtils.isEmpty(commentQueryVo.getNickName()),
                "u.nick_name", commentQueryVo.getNickName());
        // 类型
        queryWrapper.eq(!ObjectUtils.isEmpty(commentQueryVo.getType())
                        && commentQueryVo.getType().getCode() > 0,
                "c.`type`", commentQueryVo.getType());
        // 留言/评论时间
        queryWrapper.like(!ObjectUtils.isEmpty(commentQueryVo.getCommentTime()),
                "c.comment_time", commentQueryVo.getCommentTime());

        // 留言/评论目标名称（商品名称或帖子标题）
        if (!ObjectUtils.isEmpty(commentQueryVo.getTargetName())) {
            if (commentQueryVo.getType() == LTCommentType.LEAVE_MESSAGE) {
                queryWrapper.likeRight("p.`name`", commentQueryVo.getTargetName());
            } else if (commentQueryVo.getType() == LTCommentType.COMMENT) {
                queryWrapper.likeRight("t.`title`", commentQueryVo.getTargetName());
            }
        }

        // 查询并返回数据
        return baseMapper.findListByPage(page, queryWrapper);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.COMMENT, key = "#id")
    public LTCommentVo findCommentById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTCommentVo commentVo) {
        LTComment comment = this.convert(commentVo);
        if (this.save(comment)) {
            this.cacheCommentById(comment.getId());
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(value = CacheKeyConstants.COMMENT, key = "#commentVo.id")
    @Transactional(rollbackFor = Exception.class)
    public boolean update(LTCommentVo commentVo) {
        LTComment comment = this.convert(commentVo);
        return this.updateById(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.COMMENT, key = "#id")
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @CachePut(value = CacheKeyConstants.COMMENT, key = "#result.id")
    public LTCommentVo cacheCommentById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTComment convert(LTCommentVo commentVo) {
        LTComment comment = new LTComment();
        BeanUtils.copyProperties(commentVo, comment);
        return comment;
    }

    private LTCommentVo convert(LTComment comment) {
        LTCommentVo commentVo = new LTCommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        return commentVo;
    }
}
