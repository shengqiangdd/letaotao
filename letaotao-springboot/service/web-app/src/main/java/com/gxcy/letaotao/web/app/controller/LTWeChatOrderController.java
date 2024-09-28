package com.gxcy.letaotao.web.app.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.annotations.MethodExporter;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTWeChatOrderService;
import com.gxcy.letaotao.web.app.vo.LTWeChatOrderVo;
import com.gxcy.letaotao.web.app.vo.LTWechatOrderQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单控制器")
@RestController
@RequestMapping("/api/wx_orders")
public class LTWeChatOrderController {

    @Resource
    private LTWeChatOrderService ltWeChatOrderService;

    /**
     * 分页查询订单列表
     *
     * @param ltOrderQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询订单列表")
    @MethodExporter
    public Result<?> getOrderList(LTWechatOrderQueryVo ltOrderQueryVo) {
        // 创建分页对象
        IPage<LTWeChatOrderVo> page = new Page<>(ltOrderQueryVo.getPageNo(), ltOrderQueryVo.getPageSize());
        // 调用分页查询订单列表方法
        ltWeChatOrderService.findListByPage(page, ltOrderQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询订单详情")
    @MethodExporter
    public Result<?> getOrderById(@PathVariable Integer id, @RequestParam(required = false) Long currentUserId) {
        LTWeChatOrderVo orderVo = ltWeChatOrderService.getOrderRelation(id, currentUserId);
        return Result.ok(orderVo);
    }

    @GetMapping("/chat")
    @Operation(summary = "通过聊天关系id查询订单")
    public Result<?> getOrderByRelation(LTWeChatOrderVo ltWeChatOrderVO) {
        // 通过聊天关系id查询订单
        LTWeChatOrderVo ltOrder = ltWeChatOrderService.getOrderByChatRelation(ltWeChatOrderVO);
        return ltOrder != null ? Result.ok(ltOrder) : Result.error().message("订单不存在");
    }

    @PostMapping("")
    @Operation(summary = "创建订单")
    public Result<?> createOrder(@Validated @RequestBody LTWeChatOrderVo ltWeChatOrderVO) {
        // 创建订单
        LTWeChatOrderVo orderVO = ltWeChatOrderService.createOrder(ltWeChatOrderVO);
        return !ObjectUtils.isEmpty(orderVO) ? Result.ok(orderVO) : Result.error().message("创建订单失败");
    }

    @GetMapping("/checkAvailable/{productId}")
    @Operation(summary = "判断商品是否可购买")
    public Result<?> checkProductAvailability(@PathVariable Integer productId) {
        // 判断商品是否可购买
        boolean isAvailable = ltWeChatOrderService.isProductAvailableForOrder(productId);
        return isAvailable ? Result.ok() : Result.exist().message("该商品已被下单！");
    }

    @PutMapping("/pay")
    @Operation(summary = "支付订单")
    public Result<?> pay(@Validated @RequestBody LTWeChatOrderVo ltWeChatOrderVO) {
        boolean pay = ltWeChatOrderService.pay(ltWeChatOrderVO);
        return pay ? Result.ok().message("支付成功") : Result.error().message("支付失败");
    }

    @PutMapping("/deliver")
    @Operation(summary = "发货")
    public Result<?> deliver(@Validated @RequestBody LTWeChatOrderVo orderVo) {
        LTWeChatOrderVo ltWeChatOrderVo = ltWeChatOrderService.deliver(orderVo);
        return !ObjectUtils.isEmpty(ltWeChatOrderVo) ?
                Result.ok(ltWeChatOrderVo).message("发货成功") : Result.error().message("发货失败");
    }

    @PutMapping("/receive")
    @Operation(summary = "收货")
    public Result<?> receive(@Validated @RequestBody LTWeChatOrderVo orderVo) {
        LTWeChatOrderVo ltWeChatOrderVo = ltWeChatOrderService.receive(orderVo);
        return !ObjectUtils.isEmpty(ltWeChatOrderVo) ?
                Result.ok(ltWeChatOrderVo).message("收货成功") : Result.error().message("收货失败");
    }

    @PutMapping("/cancel")
    @Operation(summary = "取消订单")
    public Result<?> cancel(@Validated @RequestBody LTWeChatOrderVo orderVo) {
        LTWeChatOrderVo ltWeChatOrderVo = ltWeChatOrderService.cancel(orderVo);
        return !ObjectUtils.isEmpty(ltWeChatOrderVo) ?
                Result.ok(ltWeChatOrderVo).message("订单取消成功") : Result.error().message("订单取消失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单")
    public Result<?> deleteOrder(@PathVariable Integer id) {
        boolean delete = ltWeChatOrderService.deleteById(id);
        return delete ? Result.ok().message("订单删除成功") : Result.error().message("订单删除失败");
    }
}
