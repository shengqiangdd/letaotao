package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/11 18:57
 */
@Getter
public enum PermissionType implements BaseEnum {

    DIR(0, "目录"),
    MENU(1, "菜单"),
    BUTTON(2, "按钮");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    PermissionType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
