package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/10 10:17
 */
@Getter
public enum LTProductStatus implements BaseEnum {

    STATUS_DRAFT(0, "草稿"),
    STATUS_ON_SHELF(1, "已上架"),
    STATUS_OFF_SHELF(2, "已下架"),
    STATUS_PENDING_REVIEW(3, "待审核"),
    STATUS_SOLD_OUT(4, "已售出");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTProductStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
