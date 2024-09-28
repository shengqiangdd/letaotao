package com.gxcy.letaotao.web.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.annotations.MethodExporter;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.LTUserService;
import com.gxcy.letaotao.web.admin.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author csq
 * @Date 2024/9/14 23:10
 */
@Tag(name = "乐淘淘用户控制器")
@RestController
@RequestMapping("/api/lt_users")
public class LTUserController {

    @Resource
    private LTUserService ltUserService;

    /**
     * 分页查询用户列表
     *
     * @param userQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询用户列表")
    @MethodExporter
    public Result<?> list(LTUserQueryVo userQueryVo) {
        // 创建分页对象
        IPage<LTUserVo> page = new Page<>(userQueryVo.getPageNo(), userQueryVo.getPageSize());
        // 调用分页查询方法
        ltUserService.findUserListByPage(page, userQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询用户信息")
    public Result<?> getUserById(@PathVariable Long id) {
        LTUserVo user = ltUserService.findUserById(id);
        return user != null ? Result.ok(user).message("查询成功")
                : Result.error().message("查询失败");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('letao:user:delete')")
    public Result<?> delete(@PathVariable Long id) {
        // 调用删除用户信息的方法
        boolean delete = ltUserService.deleteById(id);
        return delete ? Result.ok().message("用户删除成功")
                : Result.error().message("用户删除失败");
    }

}
