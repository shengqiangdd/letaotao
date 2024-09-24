package com.gxcy.letaotao.web.admin.controller;


import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.PermissionService;
import com.gxcy.letaotao.web.admin.vo.PermissionQueryVo;
import com.gxcy.letaotao.web.admin.vo.PermissionVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "权限控制器")
@RestController
@RequestMapping("/api/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    /**
     * 查询菜单列表
     *
     * @param permissionQueryVo
     * @return
     */
    @GetMapping("/list")
    @Operation(description = "查询菜单列表")
    public Result<?> getMenuList(PermissionQueryVo permissionQueryVo) {
        // 查询菜单列表
        List<PermissionVo> permissionList = permissionService.findPermissionList(permissionQueryVo);
        // 返回数据
        return Result.ok(permissionList);
    }

    /**
     * 查询上级菜单列表
     *
     * @return
     */
    @GetMapping("/parent/list")
    @Operation(description = "查询上级菜单列表")
    public Result<?> getParentList() {
        // 查询上级菜单列表
        List<PermissionVo> permissionList = permissionService.findParentPermissionList();
        // 返回数据
        return Result.ok(permissionList);
    }

    /**
     * 根据id查询菜单信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(description = "根据id查询菜单信息")
    public Result<?> getMenuById(@PathVariable Long id) {
        return Result.ok(permissionService.findPermissionById(id));
    }

    /**
     * 添加菜单
     *
     * @param permissionVo
     * @return
     */
    @PostMapping("")
    @Operation(description = "添加菜单")
    @PreAuthorize("hasAuthority('sys:menu:add')")
    public Result<?> add(@RequestBody PermissionVo permissionVo) {
        boolean add = permissionService.add(permissionVo);
        return add ? Result.ok().message("菜单添加成功") : Result.error().message("菜单添加失败");
    }

    /**
     * 修改菜单
     *
     * @param permissionVo
     * @return
     */
    @PutMapping("")
    @Operation(description = "修改菜单")
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    public Result<?> update(@RequestBody PermissionVo permissionVo) {
        boolean update = permissionService.update(permissionVo);
        return update ? Result.ok().message("菜单修改成功") : Result.error().message("菜单修改失败");
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(description = "删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result<?> delete(@PathVariable Long id) {
        boolean delete = permissionService.deleteById(id);
        return delete ? Result.ok().message("菜单删除成功") : Result.error().message("菜单删除失败");
    }

    /**
     * 检查菜单下是否有子菜单
     *
     * @param id
     * @return
     */
    @GetMapping("/check/{id}")
    @Operation(description = "检查菜单下是否有子菜单")
    public Result<?> check(@PathVariable Long id) {
        // 判断菜单是否有子菜单
        if (permissionService.hasChildrenOfPermission(id)) {
            return Result.exist().message("该菜单下有子菜单，无法删除");
        }
        return Result.ok();
    }
}
