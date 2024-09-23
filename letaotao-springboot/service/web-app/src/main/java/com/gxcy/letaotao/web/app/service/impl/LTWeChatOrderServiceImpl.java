package com.gxcy.letaotao.web.app.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.config.redis.RedisService;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTOrderStatus;
import com.gxcy.letaotao.common.enums.LTProductStatus;
import com.gxcy.letaotao.common.exception.LTException;
import com.gxcy.letaotao.web.app.config.handler.OrderCancellationTaskManager;
import com.gxcy.letaotao.web.app.mapper.LTOrderMapper;
import com.gxcy.letaotao.web.app.service.*;
import com.gxcy.letaotao.web.app.vo.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class LTWeChatOrderServiceImpl extends BaseServiceImpl<LTOrderMapper, LTOrder> implements LTWeChatOrderService {

    @Resource
    private RedisService redisService;
    @Resource
    private LTWeChatProductService ltWeChatProductService;
    @Resource
    private WeChatUserService userService;
    @Resource
    private LTWeChatEvaluateService ltWeChatEvaluateService;
    @Resource
    private LTWeChatChatRelationService ltWeChatChatRelationService;
    @Resource
    private LTWeChatAddressService ltWeChatAddressService;
    @Resource
    private OrderCancellationTaskManager orderCancellationTaskManager;

    @Override
    public IPage<LTWeChatOrderVo> findListByPage(IPage<LTWeChatOrderVo> page, LTWechatOrderQueryVo ltOrderQueryVo) {
        QueryWrapper<LTWechatOrderQueryVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(ltOrderQueryVo.getBuyerId()),
                "o.buyer_id", ltOrderQueryVo.getBuyerId()); // 买家id
        queryWrapper.eq(ObjectUtils.isNotEmpty(ltOrderQueryVo.getSellerId()),
                "o.seller_id", ltOrderQueryVo.getSellerId()); // 卖家id
        queryWrapper.likeRight(ObjectUtils.isNotEmpty(ltOrderQueryVo.getSearchValue()),
                "p.`name`", ltOrderQueryVo.getSearchValue()); // 商品名称
        queryWrapper.eq(ObjectUtils.isNotEmpty(ltOrderQueryVo.getStatus())
                        && !(ltOrderQueryVo.getStatus().getCode() <= LTOrderStatus.STATUS_UNKNOWN.getCode()
                        || ltOrderQueryVo.getStatus().getCode() > LTOrderStatus.STATUS_CANCELLED.getCode()),
                "o.`status`", ltOrderQueryVo.getStatus()); // 订单状态
        queryWrapper.ne("o.status", LTOrderStatus.STATUS_CANCELLED); // 未取消
        if (ObjectUtils.isNotEmpty(ltOrderQueryVo.getStatus()) && ltOrderQueryVo.getStatus() == LTOrderStatus.STATUS_PENDING_EVALUATION) { // 待评价
            queryWrapper.notExists("select 1 from lt_evaluate e where e.order_id = o.id");
        }
        if (ObjectUtils.isNotEmpty(ltOrderQueryVo.getStartDate()) && ObjectUtils.isNotEmpty(ltOrderQueryVo.getEndDate())) { // 订单创建时间
            queryWrapper.between("o.create_time", ltOrderQueryVo.getStartDate(), ltOrderQueryVo.getEndDate());
        }
        IPage<LTWeChatOrderVo> ltOrderIPage = baseMapper.findListByPage(page, queryWrapper); // 查询分页数据
        List<LTWeChatOrderVo> ltOrderList = ltOrderIPage.getRecords();
        LTUserVo currentUser = userService.getCurrentUser();
        if (!ltOrderList.isEmpty()) {
            ltOrderList.forEach(ltOrder -> {
                LTUserVo buyer = userService.findUserById(ltOrder.getBuyerId()); // 买家
                LTUserVo seller = userService.findUserById(ltOrder.getSellerId()); // 卖家
                LTWechatProductVo ltProduct = ltWeChatProductService.getProductRelationImagesById(ltOrder.getProductId()); // 商品
                ltOrder.setBuyer(buyer);
                ltOrder.setSeller(seller);
                ltOrder.setProduct(ltProduct);
                ltOrder.setRelationId(ltWeChatChatRelationService.getOrCreateChatRelation(ltOrder.getBuyerId(),
                        ltOrder.getSellerId(), ltOrder.getProductId()).getId()); // 聊天关系id
                if (ObjectUtils.isNotEmpty(currentUser)) { // 当前用户
                    ltOrder.setIsEvaluate(ltWeChatEvaluateService.isEvaluate(ltOrder.getId(), currentUser.getId())); // 是否已评价
                }
            });
        }
        return ltOrderIPage;
    }

    @Override
    public LTWeChatOrderVo getOrderByChatRelation(LTWeChatOrderVo ltWeChatOrderVO) {
        // 根据聊天关系id获取订单
        LTWeChatOrderVo ltOrder = baseMapper.getOrderByChatRelation(ltWeChatOrderVO);
        if (ObjectUtils.isNotEmpty(ltOrder) && ObjectUtils.isNotEmpty(ltWeChatOrderVO.getCurrentUserId())) { // 当前用户
            // 是否已评价
            boolean evaluate = ltWeChatEvaluateService.isEvaluate(ltOrder.getId(), ltWeChatOrderVO.getCurrentUserId());
            ltOrder.setIsEvaluate(evaluate);
        }
        return ltOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTWeChatOrderVo createOrder(LTWeChatOrderVo ltWeChatOrderVO) {
        if (ObjectUtils.isNotEmpty(ltWeChatOrderVO)) {
            LambdaQueryWrapper<LTOrder> orderQueryWrapper = new LambdaQueryWrapper<>();
            orderQueryWrapper.eq(LTOrder::getSellerId, ltWeChatOrderVO.getSellerId());
            orderQueryWrapper.eq(LTOrder::getProductId, ltWeChatOrderVO.getProductId());
            orderQueryWrapper.ne(LTOrder::getStatus, LTOrderStatus.STATUS_CANCELLED);
            // 已存在未支付订单
            LTOrder existOrder = this.getOne(orderQueryWrapper);
            LTWechatProductVo ltWechatProductVo = ltWeChatProductService.findProductById(ltWeChatOrderVO.getProductId());
            LTUserVo currentUser = userService.getCurrentUser();
            // 判断商品是否被锁定
            if (ObjectUtils.isNotEmpty(existOrder) || ltWechatProductVo.getIsLock() == BooleanStatus.TRUE) {
                if (!Objects.equals(ltWechatProductVo.getLockBy(), currentUser.getNickName())) {
                    throw new LTException("该商品已被其他用户下单");
                } else {
                    throw new LTException("请勿重复创建订单");
                }
            }

            ltWeChatOrderVO.setStatus(LTOrderStatus.STATUS_PENDING_PAYMENT); // 待付款

            ltWechatProductVo.setIsLock(BooleanStatus.TRUE); // 锁定
            ltWechatProductVo.setLockBy(currentUser.getNickName());
            if (ltWeChatProductService.update(ltWechatProductVo)) { // 更新商品状态
                if (this.add(ltWeChatOrderVO)) { // 创建订单
                    log.info("用户{} 创建订单：{}", currentUser.getNickName()+currentUser.getOpenId() , ltWeChatOrderVO);
                    // 默认超时时间设置为30分钟
                    LocalDateTime timeout = LocalDateTime.now().plusMinutes(30);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String timeoutString = timeout.format(formatter);
                    ltWeChatOrderVO.setTimeout(timeoutString);
                    redisService.set(ltWeChatOrderVO.getBuyerId() + ":"
                            + ltWeChatOrderVO.getOrderNum(), timeoutString, (long) (30 * 60));
                    redisService.set(ltWeChatOrderVO.getBuyerId() + ":" + ltWeChatOrderVO.getProductId(),
                            ltWeChatOrderVO.getOrderNum(), (long) (30 * 60));
                    // 启动订单取消任务
                    orderCancellationTaskManager.startCancellationTask();
                    return ltWeChatOrderVO;
                }
            }
        }
        return null;
    }

    /**
     * 检查商品是否已经被锁定
     */
    @Override
    public boolean isProductAvailableForOrder(Integer productId) {
        // 查询商品是否被锁定
        return baseMapper.checkProductLockedStatus(productId) == null;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ORDER, key = "#id", unless = "#result == null")
    public LTWeChatOrderVo getOrderById(Integer id) {
        // 根据id获取订单
        return baseMapper.getOrderById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTWeChatOrderVo orderVo) {
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
    public LTWeChatOrderVo update(LTWeChatOrderVo orderVo) {
        LTOrder ltOrder = this.convert(orderVo);
        if (this.updateById(ltOrder)) {
            return this.convert(ltOrder);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ORDER, key = "#id")
    public boolean deleteById(Integer id) {
        LTOrder order = this.getById(id);
        if (!Objects.equals(LTOrderStatus.STATUS_CANCELLED, order.getStatus()) &&
                !Objects.equals(LTOrderStatus.STATUS_COMPLETED, order.getStatus())) {
            throw new LTException("该状态的订单不能被删除");
        }
        if (this.removeById(id)) { // 删除订单
            log.info("订单删除成功，订单号：{}", order.getOrderNum());
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ORDER, key = "#id", unless = "#result == null")
    public LTWeChatOrderVo getOrderRelation(Integer id, Long currentUserId) {
        // 通过id查询订单
        LTWeChatOrderVo ltOrder = baseMapper.getOrderById(id);
        if (ObjectUtils.isEmpty(ltOrder)) {
           throw new LTException("订单不存在");
        }
        // 通过商品id查询商品
        LTWechatProductVo ltProduct = ltWeChatProductService.getProductRelationImagesById(ltOrder.getProductId());
        // 通过地址id查询地址
        LTWechatAddressVo ltAddress = ltWeChatAddressService.findAddressById(ltOrder.getAddressId());
        // 拼接地址
        ltOrder.setAddressName(ltAddress.getContactPerson() + " " + ltAddress.getRegion() + " "
                + ltAddress.getDetailAddress());
        // 通过用户id查询用户
        LTUserVo buyer = userService.findUserById(ltOrder.getBuyerId());
        LTUserVo seller = userService.findUserById(ltOrder.getSellerId());
        ltOrder.setBuyer(buyer);
        ltOrder.setSeller(seller);
        ltOrder.setProduct(ltProduct);
        // 通过聊天关系id查询聊天关系并设置
        ltOrder.setRelationId(ltWeChatChatRelationService.getOrCreateChatRelation(ltOrder.getBuyerId(),
                ltOrder.getSellerId(), ltOrder.getProductId()).getId());
        if (ObjectUtils.isEmpty(currentUserId)) {
            // 判断是否评价
            boolean evaluate = ltWeChatEvaluateService.isEvaluate(ltOrder.getId(), currentUserId);
            ltOrder.setIsEvaluate(evaluate);
        }
        if (Objects.equals(ltOrder.getStatus(), LTOrderStatus.STATUS_PENDING_PAYMENT)) {
            // 获取超时时间
            String timeout = redisService.get(ltOrder.getBuyerId() + ":" + ltOrder.getOrderNum());
            ltOrder.setTimeout(timeout);
        }
        return ltOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pay(LTWeChatOrderVo ltWeChatOrderVO) {
        if (ObjectUtils.isEmpty(ltWeChatOrderVO)) {
            throw new LTException("订单不存在");
        }
        if (!Objects.equals(LTOrderStatus.STATUS_PENDING_PAYMENT, ltWeChatOrderVO.getStatus())) {
            throw new LTException("订单状态异常");
        }
        // 解锁商品
        LTWechatProductVo ltProduct = ltWeChatProductService.findProductById(ltWeChatOrderVO.getProductId());
        ltProduct.setIsLock(BooleanStatus.FALSE);
        ltProduct.setLockBy("");
        ltWeChatProductService.update(ltProduct);
        // 更新订单状态
        ltWeChatOrderVO.setStatus(LTOrderStatus.STATUS_PENDING_DELIVERY);
        ltWeChatOrderVO.setPayTime(new Timestamp(System.currentTimeMillis()));
        LTWeChatOrderVo updatedOrder = this.update(ltWeChatOrderVO);
        if (updatedOrder != null) {
            log.info("订单支付成功，订单号：{}", updatedOrder.getOrderNum());
            ltProduct.setStatus(LTProductStatus.STATUS_SOLD_OUT); // 更新商品状态
            ltWeChatProductService.update(ltProduct);
            return true;
        } else {
            log.info("订单支付失败，订单号：{}", ltWeChatOrderVO.getOrderNum());
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTWeChatOrderVo deliver(LTWeChatOrderVo orderVo) {
        return handleOrderStatus(orderVo, LTOrderStatus.STATUS_PENDING_DELIVERY, LTOrderStatus.STATUS_Pending_Received);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTWeChatOrderVo receive(LTWeChatOrderVo orderVo) {
        return handleOrderStatus(orderVo, LTOrderStatus.STATUS_Pending_Received, LTOrderStatus.STATUS_COMPLETED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTWeChatOrderVo cancel(LTWeChatOrderVo orderVo) {
        return handleOrderStatus(orderVo, LTOrderStatus.STATUS_PENDING_PAYMENT, LTOrderStatus.STATUS_CANCELLED);
    }

    @CachePut(value = CacheKeyConstants.ORDER, key = "#result.id", unless = "#result == null")
    public LTWeChatOrderVo cacheOrderById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTOrder convert(LTWeChatOrderVo orderVo) {
        LTOrder order = new LTOrder();
        BeanUtils.copyProperties(orderVo, order);
        return order;
    }

    private LTWeChatOrderVo convert(LTOrder order) {
        LTWeChatOrderVo orderVo = new LTWeChatOrderVo();
        BeanUtils.copyProperties(order, orderVo);
        return orderVo;
    }

    @Transactional(rollbackFor = Exception.class)
    protected LTWeChatOrderVo handleOrderStatus(LTWeChatOrderVo ltWeChatOrderVO, LTOrderStatus status, LTOrderStatus changeStatus) {
        if (ObjectUtils.isEmpty(ltWeChatOrderVO)) {
            throw new LTException("订单不存在");
        }
        if (!Objects.equals(status, ltWeChatOrderVO.getStatus())) {
            throw new LTException("订单状态异常");
        }
        // 更新订单状态
        ltWeChatOrderVO.setStatus(changeStatus);
        String desc = switch (changeStatus) {
            case STATUS_Pending_Received -> {
                ltWeChatOrderVO.setShipTime(new Timestamp(System.currentTimeMillis()));
                yield "发货";
            }
            case STATUS_COMPLETED -> {
                ltWeChatOrderVO.setFinishTime(new Timestamp(System.currentTimeMillis()));
                yield "收货";
            }
            case STATUS_CANCELLED -> "取消";
            default -> "";
        };
        LTWeChatOrderVo updatedOrder = this.update(ltWeChatOrderVO);
        if (updatedOrder != null) {
            log.info("订单{}成功，订单号：{}", desc ,updatedOrder.getOrderNum());
            if(updatedOrder.getStatus().equals(LTOrderStatus.STATUS_COMPLETED)
                    || updatedOrder.getStatus().equals(LTOrderStatus.STATUS_CANCELLED)) { // 订单完成或取消
                // 解锁商品
                LTProduct ltProduct = ltWeChatProductService.getById(updatedOrder.getProductId());
                ltProduct.setIsLock(BooleanStatus.FALSE);
                ltProduct.setLockBy(null);
                ltWeChatProductService.update(ltProduct);
                log.info("商品解锁成功，商品id：{}", ltProduct.getId());
            }
            return ltWeChatOrderVO;
        } else {
            log.info("订单{}失败，订单号：{}", desc, ltWeChatOrderVO.getOrderNum());
            return null;
        }
    }

    private String generateOrderNum(LTWeChatOrderVo ltWeChatOrderVO) { // 生成订单号
        // 当前时间戳
        long timestamp = Instant.now().toEpochMilli();

        // 构建订单号
        return timestamp + "" + ltWeChatOrderVO.getBuyerId()
                + ltWeChatOrderVO.getSellerId() + ltWeChatOrderVO.getProductId();
    }
}
