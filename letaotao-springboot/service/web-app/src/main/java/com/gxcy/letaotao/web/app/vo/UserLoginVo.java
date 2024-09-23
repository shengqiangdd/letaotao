package com.gxcy.letaotao.web.app.vo;

import lombok.Builder;

@Builder
public class UserLoginVo {
    private Long id;
    private String openid;
    private String token;
    private UserInfoVo userInfo;
}