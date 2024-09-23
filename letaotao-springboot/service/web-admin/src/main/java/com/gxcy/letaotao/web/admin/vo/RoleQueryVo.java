package com.gxcy.letaotao.web.admin.vo;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class RoleQueryVo {
    @Parameter(name = "当前页码")
    private Long pageNo = 1L; // 当前页码

    @Parameter(name = "每页显示数量")
    private Long pageSize = 10L; // 每页显示数量

    @Parameter(name = "用户ID")
    private Long userId; // 用户ID

    @Parameter(name = "角色名称")
    private String roleName;
}
