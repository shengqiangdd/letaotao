package com.gxcy.letaotao.web.app.controller;


import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTWeChatEvaluateService;
import com.gxcy.letaotao.web.app.vo.LTWechatEvaluateVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评价控制器")
@RestController
@RequestMapping("/api/wx_evaluates")
public class LTWeChatEvaluateController {

    @Resource
    private LTWeChatEvaluateService ltWeChatEvaluateService;

    @Operation(summary = "通过用户Id查询评价列表")
    @GetMapping("/list/user/{userId}")
    public Result<?> getEvaluateListByUserId(@PathVariable Long userId) {
        List<LTWechatEvaluateVo> list = ltWeChatEvaluateService.getEvaluateListByUserId(userId);
        return Result.ok(list);
    }

    @Operation(summary = "通过订单Id查询评价列表")
    @GetMapping("/list/order/{orderId}")
    public Result<?> getEvaluateListByOrderId(@PathVariable Long orderId) {
        List<LTWechatEvaluateVo> list = ltWeChatEvaluateService.getEvaluateListByOrderId(orderId);
        return Result.ok(list);
    }

    @PostMapping("")
    @Operation(summary = "评价订单")
    public Result<?> addEvaluate(@Validated @RequestBody LTWechatEvaluateVo ltEvaluateVo) {
        LTWechatEvaluateVo evaluateVo = ltWeChatEvaluateService.add(ltEvaluateVo);
        return evaluateVo != null ? Result.ok(ltEvaluateVo).message("评价成功") : Result.error().message("评价失败");
    }

    @Operation(summary = "判断是否评价")
    @GetMapping("/isEvaluate")
    public Result<?> isEvaluate(Integer orderId, Long userId) {
        boolean evaluate = ltWeChatEvaluateService.isEvaluate(orderId, userId);
        return Result.ok(evaluate).message(evaluate ? "已评价" : "未评价");
    }

}
