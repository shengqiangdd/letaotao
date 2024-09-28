package com.gxcy.letaotao.web.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.annotations.MethodExporter;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.dto.UserRoleDTO;
import com.gxcy.letaotao.web.admin.entity.Role;
import com.gxcy.letaotao.web.admin.entity.UserInfo;
import com.gxcy.letaotao.web.admin.service.RoleService;
import com.gxcy.letaotao.web.admin.service.UserService;
import com.gxcy.letaotao.web.admin.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "用户控制器")
@RestController
@RequestMapping("/api/sysUser")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    /**
     * 分页查询用户列表
     *
     * @param userQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询用户列表")
    @MethodExporter
    public Result<?> list(UserQueryVo userQueryVo) {
        // 创建分页对象
        IPage<UserVo> page = new Page<>(userQueryVo.getPageNo(), userQueryVo.getPageSize());
        // 调用分页查询方法
        userService.findUserListByPage(page, userQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    /**
     * 用户登录
     *
     * @param user 用户
     * @return
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public void login(@Validated @RequestBody LoginVo user, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        userService.login(user, request, response);
    }

    /**
     * 用户退出
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("logout")
    @Operation(summary = "用户退出")
    public Result<?> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return Result.ok().message("用户退出成功");
    }

    /**
     * 查询所有用户列表
     *
     * @return
     */
    @Operation(summary = "获取用户列表")
    @GetMapping("/listAll")
    public Result<?> listAll() {
        return Result.ok(userService.list());
    }

    /**
     * 刷新token
     *
     * @param request
     * @return
     */
    @Operation(summary = "刷新token")
    @PostMapping("/refreshToken")
    public Result<?> refreshToken(HttpServletRequest request) {
        TokenVo tokenVo = userService.refreshToken(request);
        //返回数据
        return Result.ok(tokenVo).message("token生成成功");
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Operation(summary = "获取用户信息", description = "获取用户信息")
    @GetMapping("/info")
    public Result<?> getInfo() {
        // 获取用户信息
        UserInfo userInfo = userService.getUserInfo();
        // 返回数据
        return Result.ok(userInfo).message("用户信息查询成功");
    }

    /**
     * 获取菜单路由数据
     *
     * @return
     */
    @Operation(summary = "获取菜单路由数据", description = "获取菜单路由数据")
    @GetMapping("/menu")
    @MethodExporter
    public Result<?> getMenuList() {
        List<RouterVo> routerList = userService.getMenuList();
        // 返回数据
        return Result.ok(routerList).message("菜单数据获取成功");
    }

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     */
    @PostMapping("")
    @Operation(summary = "添加用户")
    @PreAuthorize("hasAuthority('sys:user:add')")
    public Result<?> add(@RequestBody UserVo userVo) {
        boolean add = userService.add(userVo);
        return add ? Result.ok().message("用户添加成功")
                : Result.error().message("用户添加失败");
    }

    /**
     * 修改用户
     *
     * @param userVo
     * @return
     */
    @PutMapping("")
    @Operation(summary = "修改用户")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public Result<?> update(@RequestBody UserVo userVo) {
        // 调用修改用户信息的方法
        boolean update = userService.update(userVo);
        return update ? Result.ok().message("用户修改成功")
                : Result.error().message("用户修改失败");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<?> delete(@PathVariable Long id) {
        // 调用删除用户信息的方法
        boolean delete = userService.deleteById(id);
        return delete ? Result.ok().message("用户删除成功")
                : Result.error().message("用户删除失败");
    }

    /**
     * 读取分配角色列表
     *
     * @param roleQueryVo
     * @return
     */
    @GetMapping("/list/assign/role")
    @Operation(summary = "获取角色列表")
    @PreAuthorize("hasAuthority('sys:user:assign')")
    @MethodExporter
    public Result<?> getRoleListForAssign(RoleQueryVo roleQueryVo) {
        // 创建分页对象
        IPage<Role> page = new Page<>(roleQueryVo.getPageNo(), roleQueryVo.getPageSize());
        // 调用查询方法
        roleService.findRoleListByUserId(page, roleQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    /**
     * 根据用户ID查询该用户拥有的角色列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/role/user/{userId}")
    @Operation(summary = "根据用户ID查询该用户拥有的角色列表")
    @PreAuthorize("hasAuthority('sys:user:assign')")
    public Result<?> getRoleByUserId(@PathVariable Long userId) {
        // 调用根据用户ID查询该用户拥有的角色ID的方法
        List<Long> roleIds = roleService.findRoleIdByUserId(userId);
        return Result.ok(roleIds);
    }

    /**
     * 分配角色
     *
     * @param userRoleDTO
     * @return
     */
    @PostMapping("/saveUserRole")
    @PreAuthorize("hasAuthority('sys:user:assign')")
    public Result<?> saveUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        if (userService.saveUserRole(userRoleDTO.getUserId(), userRoleDTO.getRoleIds())) {
            return Result.ok().message("角色分配成功");
        }
        return Result.error().message("角色分配失败");
    }
}
