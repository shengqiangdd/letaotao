package com.gxcy.letaotao.web.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxcy.letaotao.common.entity.LTComment;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.web.admin.mapper.*;
import com.gxcy.letaotao.web.admin.service.StatisticsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private LTProductMapper productMapper;
    @Resource
    private LTCommentMapper commentMapper;
    @Resource
    private LTPostMapper postMapper;
    @Resource
    private LTOrderMapper orderMapper;


    private <T> QueryWrapper<T> createQueryWrapper(String timeField, Integer year, Integer month) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("YEAR(" + timeField + ") = {0}", year); // 指定年份
        if (!ObjectUtils.isEmpty(month)) {
            queryWrapper.apply("MONTH(" + timeField + ") = {0}", month); // 指定月份
        }
        return queryWrapper;
    }

    @Transactional(readOnly = true)
    @Override
    public long countRegisteredUsers(Integer year, Integer month) {
        // 统计注册用户数
        return userMapper.selectCount(createQueryWrapper("create_time", year, month));
    }

    @Transactional(readOnly = true)
    @Override
    public long countProductsOnSale(Integer year, Integer month) {
        // 统计上架商品数
        return productMapper.selectCount(createQueryWrapper("publish_time", year, month));
    }

    @Transactional(readOnly = true)
    @Override
    public long countProductComments(Integer year, Integer month) {
        // 统计留言数
        QueryWrapper<LTComment> queryWrapper = createQueryWrapper("comment_time", year, month);
        queryWrapper.eq("`type`", LTCommentType.LEAVE_MESSAGE);
        return commentMapper.selectCount(queryWrapper);
    }

    @Transactional(readOnly = true)
    @Override
    public long countOrders(Integer year, Integer month) {
        // 统计订单数
        return orderMapper.selectCount(createQueryWrapper("create_time", year, month));
    }

    @Transactional(readOnly = true)
    @Override
    public long countPosts(Integer year, Integer month) {
        // 统计帖子数
        return postMapper.selectCount(createQueryWrapper("post_time", year, month));
    }

    @Transactional(readOnly = true)
    @Override
    public long countPostComments(Integer year, Integer month) {
        // 统计评论数
        QueryWrapper<LTComment> queryWrapper = createQueryWrapper("comment_time", year, month);
        queryWrapper.eq("`type`", LTCommentType.COMMENT);
        return commentMapper.selectCount(queryWrapper);
    }

}
