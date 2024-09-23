package com.gxcy.letaotao.web.admin.vo;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RolePermissionVo {
    @Parameter(name = "菜单数据")
    private List<PermissionVo> permissionList = new ArrayList<>();

    @Parameter(name = "该角色原有分配的菜单数据")
    private Object[] checkList;
}
