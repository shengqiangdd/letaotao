package com.gxcy.letaotao.web.app.utils;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 微信登录返回参数
 */
@Data
public class WeChatSessionResponse {
    private String openid; // 微信用户唯一标识
    @JSONField(name = "session_key")
    private String sessionKey; // 会话密钥

}
