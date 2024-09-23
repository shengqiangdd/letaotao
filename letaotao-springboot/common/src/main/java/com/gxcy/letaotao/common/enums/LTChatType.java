package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/12 13:08
 */
@Getter
public enum LTChatType implements BaseEnum {

    CHAT("chat", "私聊"),
    READ("read", "已读"),
    ORDER("order", "订单");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    LTChatType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
