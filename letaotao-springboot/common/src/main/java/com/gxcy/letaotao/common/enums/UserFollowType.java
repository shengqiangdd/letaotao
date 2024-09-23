package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/11 19:05
 */
@Getter
public enum UserFollowType implements BaseEnum {

    FOLLOWERS("followers", "粉丝"),
    FOLLOWING("following", "关注者");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    UserFollowType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
