package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/10 10:15
 */
@Getter
public enum LTPostStatus implements BaseEnum {

    STATUS_DRAFT(0, "草稿"),
    STATUS_PUBLISHED(1, "已发布"),
    STATUS_PENDING_REVIEW(3, "待审核");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTPostStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
