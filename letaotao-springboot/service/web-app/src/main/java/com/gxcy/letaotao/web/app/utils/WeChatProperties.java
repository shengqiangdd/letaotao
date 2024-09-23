package com.gxcy.letaotao.web.app.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 小程序配置
 */
@Data
@ConfigurationProperties(prefix = "wechat")
@Component
public class WeChatProperties {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.default-nick-name}")
    private String defaultNickName;

    @Value("${wechat.default-avatar-url}")
    private String defaultAvatarUrl;

}
