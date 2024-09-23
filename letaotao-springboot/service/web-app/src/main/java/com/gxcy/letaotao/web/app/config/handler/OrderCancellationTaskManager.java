package com.gxcy.letaotao.web.app.config.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.config.redis.RedisService;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTOrderStatus;
import com.gxcy.letaotao.web.app.mapper.LTOrderMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatProductService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * 订单取消任务管理器
 */
@Slf4j
@Component
@EnableScheduling
public class OrderCancellationTaskManager {

    private final ScheduledTaskManager scheduledTaskManager;
    @Resource
    private LTOrderMapper ltOrderMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private LTWeChatProductService ltWeChatProductService;

    @Autowired
    public OrderCancellationTaskManager(ScheduledTaskManager scheduledTaskManager) {
        this.scheduledTaskManager = scheduledTaskManager;
    }

    // 添加订单时启动定时任务
    public void startCancellationTask() {
        // 每60秒钟执行一次检查
        scheduledTaskManager.registerTask(this::cancelExpiredOrders, Duration.ofMinutes(1));
    }

    // 取消订单时停止定时任务
    public void stopCancellationTask() {
        scheduledTaskManager.cancelTask();
    }

    // 定时任务方法
    @Transactional(rollbackFor = Exception.class)
    public void cancelExpiredOrders() {
        LambdaQueryWrapper<LTOrder> orderMapper = new LambdaQueryWrapper<>();
        orderMapper.and(wrapper -> {
            wrapper.eq(LTOrder::getStatus, LTOrderStatus.STATUS_PENDING_PAYMENT);
            wrapper.ne(LTOrder::getStatus, LTOrderStatus.STATUS_CANCELLED);
        });

        // 获取待支付订单数量
        long pendingOrdersCount = ltOrderMapper.selectCount(orderMapper);

        if (pendingOrdersCount == 0) {
            // 没有待支付订单，直接返回
            return;
        }

        List<LTOrder> ltOrders = ltOrderMapper.selectList(orderMapper);
        for (LTOrder ltOrder : ltOrders) {
            boolean expire = redisService.isExpire(ltOrder.getBuyerId() + ":" + ltOrder.getProductId());
            if (expire) {
                // 超时未支付，取消订单
                ltOrder.setStatus(LTOrderStatus.STATUS_CANCELLED);
                if (ltOrderMapper.updateById(ltOrder) > 0) {
                    log.info("订单超时未支付，取消订单：{}", ltOrder.getOrderNum());
                    // 解锁订单
                    LTProduct ltProduct = ltWeChatProductService.getById(ltOrder.getProductId());
                    ltProduct.setIsLock(BooleanStatus.FALSE);
                    ltProduct.setLockBy(null);
                    ltWeChatProductService.updateById(ltProduct);
                    redisService.del(CacheKeyConstants.PRODUCT + ltOrder.getProductId()); // 删除缓存
                    stopCancellationTask();
                }
            }
        }
    }
}
