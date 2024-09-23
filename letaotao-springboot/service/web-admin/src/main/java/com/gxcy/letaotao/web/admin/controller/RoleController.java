package com.gxcy.letaotao.web.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.dto.RolePermissionDTO;
import com.gxcy.letaotao.web.admin.entity.Role;
import com.gxcy.letaotao.web.admin.service.PermissionService;
import com.gxcy.letaotao.web.admin.service.RoleService;
import com.gxcy.letaotao.web.admin.vo.RolePermissionVo;
import com.gxcy.letaotao.web.admin.vo.RoleQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(name = "角色控制器")
@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;

    /**
     * 分页查询角色列表
     *
     * @param roleQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询角色列表")
    public Result<?> list(RoleQueryVo roleQueryVo) {
        // 创建分页对象
        IPage<Role> page = new Page<>(roleQueryVo.getPageNo(), roleQueryVo.getPageSize());
        // 调用分页查询方法
        roleService.findRoleListByUserId(page, roleQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    /**
     * 分配权限-查询权限树数据
     *
     * @param userId
     * @param roleId
     * @return
     */
    @GetMapping("/assign/permission/tree")
    @Operation(summary = "分配权限-查询权限树数据")
    public Result<?> getAssignPermissionTree(Long userId, Long roleId) {
        // 调用查询权限树数据的方法
        RolePermissionVo permissionTree = permissionService.findPermissionTree(userId, roleId);
        // 返回数据
        return Result.ok(permissionTree);
    }

    /**
     * 分配权限-保存权限数据
     *
     * @param rolePermissionDTO
     * @return
     */
    @PostMapping("/saveRoleAssign")
    @Operation(summary = "分配权限-保存权限数据")
    public Result<?> saveRoleAssign(@RequestBody RolePermissionDTO rolePermissionDTO) {
        if (roleService.saveRolePermission(rolePermissionDTO.getRoleId(), rolePermissionDTO.getList())) {
            return Result.ok().message("权限分配成功");
        } else {
            return Result.error().message("权限分配失败");
        }
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @PostMapping("")
    @Operation(summary = "添加角色")
    @PreAuthorize("hasAuthority('sys:role:add')")
    public Result<?> add(@RequestBody Role role) {
        boolean save = roleService.add(role);
        return save ? Result.ok().message("角色添加成功")
                : Result.error().message("角色添加失败");
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @PutMapping("")
    @Operation(summary = "修改角色")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result<?> update(@RequestBody Role role) {
        boolean delete = roleService.updateById(role);
        return delete ? Result.ok().message("角色修改成功")
                : Result.error().message("角色修改失败");
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<?> delete(@PathVariable Long id) {
        boolean delete = roleService.deleteById(id);
        return delete ? Result.ok().message("角色删除成功")
                : Result.error().message("角色删除失败");
    }
}
