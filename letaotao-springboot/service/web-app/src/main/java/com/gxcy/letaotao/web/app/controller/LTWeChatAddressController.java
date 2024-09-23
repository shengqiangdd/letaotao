package com.gxcy.letaotao.web.app.controller;


import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTWeChatAddressService;
import com.gxcy.letaotao.web.app.vo.LTWechatAddressVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "地址控制器")
@RestController
@RequestMapping("/api/wx_addresses")
public class LTWeChatAddressController {

    @Resource
    private LTWeChatAddressService ltAddressService;

    /**
     * 查询地址列表
     *
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "通过用户id查询地址列表")
    public Result<?> getAddressList(Long userId) {
        // 查询地址列表
        List<LTWechatAddressVo> ltAddressList = ltAddressService.findAddressList(userId);
        // 返回数据
        return Result.ok(ltAddressList);
    }

    /**
     * 根据id查询地址信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询地址信息")
    public Result<?> getAddressById(@PathVariable Integer id) {
        return Result.ok(ltAddressService.findAddressById(id));
    }

    @GetMapping("/default/{userId}")
    @Operation(summary = "根据用户id查询默认地址")
    public Result<?> getDefaultAddress(@PathVariable Long userId) {
        // 查询默认地址
        LTWechatAddressVo address = ltAddressService.findDefaultAddress(userId);
        return address != null ? Result.ok(address).message("查询成功")
                : Result.error().message("该用户没有默认地址");
    }

    /**
     * 添加地址
     *
     * @param addressVo
     * @return
     */
    @PostMapping("")
    @Operation(summary = "添加地址")
    public Result<?> add(@Validated @RequestBody LTWechatAddressVo addressVo) {
        if (ltAddressService.add(addressVo)) {
            return Result.ok().message("地址添加成功");
        } else {
            return Result.error().message("地址添加失败");
        }
    }

    /**
     * 修改地址
     *
     * @param addressVo
     * @return
     */
    @PutMapping("")
    @Operation(summary = "修改地址")
    public Result<?> update(@Validated @RequestBody LTWechatAddressVo addressVo) {
        if (ltAddressService.update(addressVo)) {
            return Result.ok().message("地址修改成功");
        } else {
            return Result.error().message("地址修改失败");
        }
    }

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除地址")
    public Result<?> delete(@PathVariable Integer id) {
        if (ltAddressService.deleteById(id)) {
            return Result.ok().message("地址删除成功");
        } else {
            return Result.error().message("地址删除失败");
        }
    }
}