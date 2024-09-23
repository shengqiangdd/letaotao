package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BooleanStatus implements BaseEnum {
    TRUE(1),
    FALSE(0);

    @EnumValue
    @JsonValue
    private final Integer code;

    BooleanStatus(Integer code) {
        this.code = code;
    }

    @Override
    public String getDesc() {
        return "";
    }
}