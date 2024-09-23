package com.gxcy.letaotao.web.admin.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.util.List;

/**
 * 用于给角色分配权限时保存 选中的权限数据
 */
@Data
public class RolePermissionDTO {
    @Parameter(name = "角色编号")
    private Long roleId;//角色编号
    @Parameter(name = "权限菜单ID集合")
    private List<Long> list;//权限菜单ID集合
}
