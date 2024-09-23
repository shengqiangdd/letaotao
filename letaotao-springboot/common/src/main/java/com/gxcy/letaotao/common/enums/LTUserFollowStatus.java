package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/10 20:47
 */
@Getter
public enum LTUserFollowStatus implements BaseEnum {

    STATUS_FOLLOW(1, "已关注"),
    STATUS_UNFOLLOW(0, "未关注");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTUserFollowStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据当前状态返回相反的状态。
     *
     * @param currentStatus 当前的状态
     * @return 相反的状态
     */
    public static LTUserFollowStatus toggle(LTUserFollowStatus currentStatus) {
        return currentStatus == STATUS_UNFOLLOW ? STATUS_FOLLOW : STATUS_UNFOLLOW;
    }
}
