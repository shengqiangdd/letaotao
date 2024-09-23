package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/11 18:47
 */
@Getter
public enum LTImagesType implements BaseEnum {

    PRODUCT("product", "商品"),
    POST("post", "帖子"),
    CHAT("chat", "聊天"),
    USER("user", "用户");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    LTImagesType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
