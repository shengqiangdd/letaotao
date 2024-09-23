package com.gxcy.letaotao.common.exception;

import lombok.Getter;

@Getter
public enum BizExceptionEnum {

    /**
     * 5xx：系统异常码
     */
    BIZ_ERROR(500, "系统异常"),

    /**
     * 2xxxx：用户相关异常码
     */
    USERNAME_PASSWORD_ERROR(20001, "账号密码错误"),
    USER_NOT_EXISTS(20002, "用户不存在"),
    USER_EXISTS(20003, "用户已存在"),
    USER_PASSWORD_ERROR(20003, "密码不正确"),
    USER_LOCKED(20005, "用户已被锁定"),
    USER_NOT_LOGIN(20006, "用户未登录"),
    USER_NOT_ENABLE(20007, "用户未启用"),
    USER_NOT_AUTHORIZED(20008, "用户未授权"),
    USER_NOT_PERMISSION(20009, "用户无权限"),
    USER_NOT_ROLE(20010, "用户未角色"),
    USER_NOT_PERMISSION_ROLE(20011, "用户未权限角色"),
    USER_NOT_PERMISSION_MENU(20012, "用户未权限菜单"),
    USER_NOT_PERMISSION_API(20013, "用户未权限接口"),
    USER_NOT_PERMISSION_RESOURCE(20014, "用户未权限资源"),

    /**
     * 3xxxx：参数相关异常码
     */
    PARAM_VALID_ERROR(30001, "参数校验错误"),
    PARAM_NOT_NULL(30002, "参数不能为空"),
    PARAM_NOT_EXISTS(30003, "参数不存在"),
    PARAM_NOT_VALID(30004, "参数格式不正确"),
    PARAM_NOT_NUMBER(30005, "参数必须为数字"),
    PARAM_NOT_POSITIVE(30006, "参数必须为正数"),
    PARAM_NOT_NEGATIVE(30007, "参数必须为负数"),
    PARAM_NOT_BETWEEN(30008, "参数必须在指定范围内"),

    /**
     * 4xxxx：token相关异常码
     */
    TOKEN_NOT_EXISTS(40001, "token不存在"),
    TOKEN_EXPIRED(40002, "token已过期"),
    TOKEN_INVALID(40003, "token无效"),
    TOKEN_NOT_MATCH(40004, "token不匹配");

    private final Integer code; // 状态码
    private final String msg; // 提示信息

    BizExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}