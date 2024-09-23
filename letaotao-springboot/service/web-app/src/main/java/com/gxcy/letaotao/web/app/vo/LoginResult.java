package com.gxcy.letaotao.web.app.vo;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult implements Serializable {
    @Parameter(name = "用户编号")
    private Long id; // 用户编号

    @Parameter(name = "状态码")
    private int code; // 状态码

    @Parameter(name = "token令牌")
    private String token; // token令牌

    @Parameter(name = "用户信息")
    private UserInfoVo userInfo; // 用户信息

    @Parameter(name = "token过期时间")
    private Long expireTime; // token过期时间
}
