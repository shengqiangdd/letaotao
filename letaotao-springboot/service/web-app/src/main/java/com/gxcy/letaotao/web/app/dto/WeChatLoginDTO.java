package com.gxcy.letaotao.web.app.dto;

import lombok.Data;

/**
 * 微信登录参数
 */
@Data
public class WeChatLoginDTO {
    private String code; // 小程序登录凭证
    private UserInfoDTO userInfo; // 用户信息
}