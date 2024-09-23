package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/11 18:51
 */
@Getter
public enum LTLikeTargetType implements BaseEnum {

    POST(1, "帖子"),
    COMMENT(2, "评论"),
    LEAVE_MESSAGE(3, "留言");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTLikeTargetType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
