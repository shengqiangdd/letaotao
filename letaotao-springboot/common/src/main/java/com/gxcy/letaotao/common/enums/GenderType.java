package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/11 19:01
 */
@Getter
public enum GenderType implements BaseEnum {

    MALE(0, "男"),
    FEMALE(1, "女");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    GenderType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
