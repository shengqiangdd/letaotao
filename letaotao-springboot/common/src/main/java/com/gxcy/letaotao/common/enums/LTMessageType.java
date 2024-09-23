package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/10 22:14
 */
@Getter
public enum LTMessageType implements BaseEnum {
    NOTIFICATION(1, "通知"),
    MESSAGE(2, "消息");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTMessageType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
