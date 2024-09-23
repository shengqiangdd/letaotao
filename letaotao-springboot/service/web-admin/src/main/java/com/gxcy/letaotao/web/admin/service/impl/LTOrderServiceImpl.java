package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.web.admin.mapper.LTOrderMapper;
import com.gxcy.letaotao.web.admin.mapper.LTUserMapper;
import com.gxcy.letaotao.web.admin.service.LTOrderService;
import com.gxcy.letaotao.web.admin.vo.LTOrderQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTOrderVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单表 服务实现类
 */
@Service
public class LTOrderServiceImpl extends ServiceImpl<LTOrderMapper, LTOrder> implements LTOrderService {

    @Resource
    private LTUserMapper ltUserMapper;

    @Override
    @Cacheable(value = CacheKeyConstants.ORDER, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<LTOrderVo> findListByPage(IPage<LTOrderVo> page, LTOrderQueryVo orderQueryVo) {
        // 创建条件构造器对象
        QueryWrapper<LTOrderQueryVo> queryWrapper = new QueryWrapper<>();

        // 订单号
        queryWrapper.eq(!ObjectUtils.isEmpty(orderQueryVo.getOrderNum()),
                "o.order_num", orderQueryVo.getOrderNum());
        // 商品名称
        queryWrapper.likeRight(!ObjectUtils.isEmpty(orderQueryVo.getProductName()),
                "p.`name`", orderQueryVo.getProductName());
        // 订单状态
        queryWrapper.eq(!ObjectUtils.isEmpty(orderQueryVo.getStatus())
                        && orderQueryVo.getStatus().getCode() > 0,
                "o.`status`", orderQueryVo.getStatus());

        // 排序
//        queryWrapper.orderBy(true, "create_time");

        IPage<LTOrderVo> orderIPage = baseMapper.findListByPage(page, queryWrapper);
        List<LTOrderVo> ltOrders = orderIPage.getRecords();
        for (LTOrderVo ltOrder : ltOrders) {
            ltOrder.setSellerName(ltUserMapper.selectById(ltOrder.getSellerId()).getNickName());
            ltOrder.setBuyerName(ltUserMapper.selectById(ltOrder.getBuyerId()).getNickName());
        }

        return orderIPage;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ORDER, key = "#id", unless = "#result == null")
    public LTOrderVo findOrderById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTOrderVo orderVo) {
        LTOrder order = this.convert(orderVo);
        if (this.save(order)) {
            this.cacheOrderById(order.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ORDER, key = "#orderVo.id")
    public boolean update(LTOrderVo orderVo) {
        return this.updateById(this.convert(orderVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ORDER, key = "#id")
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @CachePut(value = CacheKeyConstants.ORDER, key = "#result.id", unless = "#result == null")
    public LTOrderVo cacheOrderById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTOrder convert(LTOrderVo orderVo) {
        LTOrder order = new LTOrder();
        BeanUtils.copyProperties(orderVo, order);
        return order;
    }

    private LTOrderVo convert(LTOrder order) {
        LTOrderVo orderVo = new LTOrderVo();
        BeanUtils.copyProperties(order, orderVo);
        return orderVo;
    }
}
