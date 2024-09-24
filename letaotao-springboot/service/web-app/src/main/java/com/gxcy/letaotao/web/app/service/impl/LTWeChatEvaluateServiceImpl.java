package com.gxcy.letaotao.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.common.enums.LTOrderStatus;
import com.gxcy.letaotao.web.app.entity.LTEvaluate;
import com.gxcy.letaotao.web.app.mapper.LTEvaluateMapper;
import com.gxcy.letaotao.web.app.mapper.LTOrderMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatEvaluateService;
import com.gxcy.letaotao.web.app.vo.LTWechatEvaluateVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 评价表 服务实现类
 */
@Service
@Slf4j
public class LTWeChatEvaluateServiceImpl extends ServiceImpl<LTEvaluateMapper, LTEvaluate> implements LTWeChatEvaluateService {

    @Resource
    private LTOrderMapper ltOrderMapper;
    @Resource
    private CacheManager cacheManager;

    @Override
    public List<LTWechatEvaluateVo> getEvaluateList(LambdaQueryWrapper<LTEvaluate> queryWrapper) {
        // 查询评价列表
        return baseMapper.listByWrapper(queryWrapper);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.EVALUATE, key = "'list_'+#userId", unless = "#result == null")
    public List<LTWechatEvaluateVo> getEvaluateListByUserId(Long userId) {
        // 通过用户ID查询评价列表 查询用户已买卖的订单
        LambdaQueryWrapper<LTEvaluate> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<LTOrder> orderQueryWrapper = new LambdaQueryWrapper<>();
        orderQueryWrapper.and(o -> o.eq(LTOrder::getSellerId, userId).or(o1 -> o1.eq(LTOrder::getBuyerId, userId))); // 卖家ID或买家ID
        orderQueryWrapper.and(o -> o.eq(LTOrder::getStatus, LTOrderStatus.STATUS_COMPLETED)
                .notIn(LTOrder::getStatus, LTOrderStatus.STATUS_CANCELLED, LTOrderStatus.STATUS_PENDING_PAYMENT));
        List<LTOrder> ltOrders = ltOrderMapper.selectList(orderQueryWrapper);
        // 订单列表卖家ID
        List<Long> sellerOrders = ltOrders.stream().map(LTOrder::getSellerId).toList();
        // 订单列表买家ID
        List<Long> buyerOrders = ltOrders.stream().map(LTOrder::getBuyerId).toList();

//        ArrayList<Integer> orderIds = ltOrders
//                .stream().map(LTOrder::getId).collect(Collectors.toCollection(ArrayList::new));

        queryWrapper.in(LTEvaluate::getOrderId, ltOrders.stream().map(LTOrder::getId).toList());
        List<LTWechatEvaluateVo> evaluateList = getEvaluateList(queryWrapper);
        // 遍历评价列表 根据用户ID设置卖家或买家
        evaluateList.forEach(e -> {
            if (sellerOrders.contains(e.getUserId())) {
                e.setTag("卖家");
            } else if (buyerOrders.contains(e.getUserId())) {
                e.setTag("买家");
            }
        });
        return evaluateList;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.EVALUATE, key = "'list_'+#orderId", unless = "#result == null")
    public List<LTWechatEvaluateVo> getEvaluateListByOrderId(Long orderId) {
        // 通过订单ID查询评价列表
        LambdaQueryWrapper<LTEvaluate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(orderId),
                LTEvaluate::getOrderId, orderId);
        return getEvaluateList(queryWrapper);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.EVALUATE, key = "'bool_'+#orderId+'_'+#userId", unless = "#result == null")
    public boolean isEvaluate(Integer orderId, Long userId) {
        // 通过订单ID和用户ID查询评价列表
        return baseMapper.isEvaluate(orderId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTWechatEvaluateVo add(LTWechatEvaluateVo evaluateVo) {
        LTEvaluate evaluate = convert(evaluateVo);
        if (this.save(evaluate)) {
            this.deleteCacheEvaluate(evaluateVo);
            return this.convert(evaluate);
        }
        return null;
    }

    public void deleteCacheEvaluate(LTWechatEvaluateVo evaluateVo) {
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.EVALUATE)).evict("list_" + evaluateVo.getOrderId());
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.EVALUATE)).evict("list_" + evaluateVo.getUserId());
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.EVALUATE))
                .evictIfPresent("bool_" + evaluateVo.getOrderId() + '_' + evaluateVo.getUserId());
    }

    private LTEvaluate convert(LTWechatEvaluateVo evaluateVo) {
        LTEvaluate evaluate = new LTEvaluate();
        BeanUtils.copyProperties(evaluateVo, evaluate);
        return evaluate;
    }

    private LTWechatEvaluateVo convert(LTEvaluate evaluate) {
        LTWechatEvaluateVo evaluateVo = new LTWechatEvaluateVo();
        BeanUtils.copyProperties(evaluate, evaluateVo);
        return evaluateVo;
    }
}
