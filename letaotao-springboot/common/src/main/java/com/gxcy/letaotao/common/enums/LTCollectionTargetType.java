package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/11 18:42
 */
@Getter
public enum LTCollectionTargetType implements BaseEnum {

    PRODUCT(1, "商品"),
    POST(2, "帖子");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTCollectionTargetType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
