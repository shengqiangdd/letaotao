package com.gxcy.letaotao.web.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.LTOrderService;
import com.gxcy.letaotao.web.admin.vo.LTOrderQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTOrderVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单控制器")
@RestController
@RequestMapping("/api/orders")
public class LTOrderController {

    @Resource
    private LTOrderService ltOrderService;

    /**
     * 分页查询订单列表
     *
     * @param orderQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询订单列表")
    public Result<?> getOrderList(LTOrderQueryVo orderQueryVo) {
        // 创建分页对象
        IPage<LTOrderVo> page = new Page<>(orderQueryVo.getPageNo(), orderQueryVo.getPageSize());
        // 调用分页查询订单列表方法
        ltOrderService.findListByPage(page, orderQueryVo);
        // 返回数据
        return Result.ok(page);
    }


    /**
     * 根据id查询订单信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询订单信息")
    public Result<?> getOrderById(@PathVariable Integer id) {
        return Result.ok(ltOrderService.findOrderById(id));
    }

    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单")
    @PreAuthorize("hasAuthority('ltt:order:delete')")
    public Result<?> delete(@PathVariable Integer id) {
        boolean delete = ltOrderService.deleteById(id);
        return delete ? Result.ok().message("订单删除成功")
                : Result.error().message("订单删除失败");
    }

}