package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/11 18:45
 */
@Getter
public enum LTCommentType implements BaseEnum {

    LEAVE_MESSAGE(1, "留言"),
    COMMENT(2, "评论");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTCommentType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
