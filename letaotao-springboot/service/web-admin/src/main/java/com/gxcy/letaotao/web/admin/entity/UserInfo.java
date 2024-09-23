package com.gxcy.letaotao.web.admin.entity;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Parameter(name = "用户ID")
    private Long id; // 用户ID

    @Parameter(name = "用户名称")
    private String name; // 用户名称

    @Parameter(name = "头像")
    private String avatar; // 头像

    @Parameter(name = "介绍")
    private String introduction; // 介绍

    @Parameter(name = "角色权限集合")
    private Object[] roles; // 角色权限集合
}
