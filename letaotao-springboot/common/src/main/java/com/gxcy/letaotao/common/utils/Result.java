package com.gxcy.letaotao.common.utils;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局统一返回结果类
 *
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {
    @Parameter(name = "是否成功")
    private Boolean success; // 是否成功
    @Parameter(name = "状态码")
    private Integer code; // 状态码
    @Parameter(name = "返回消息")
    private String message; // 返回消息
    @Parameter(name = "data")
    private T data;

    /**
     * 私有化构造方法，禁止在其他类创建对象
     */
    private Result() {

    }

    /**
     * 成功执行，不返回数据
     */
    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("执行成功");
        return result;
    }

    /**
     * 执行成功，返回数据
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("执行成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败
     */
    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("执行失败");
        return result;
    }

    /**
     * 失败
     */
    public static <T> Result<T> error(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("执行失败");
        result.setData(data);
        return result;
    }

    /**
     * 设置是否成功
     *
     * @param success
     */
    public Result<T> success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    /**
     * 设置状态码
     *
     * @param code
     */
    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 设置返回消息
     *
     * @param message
     */
    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 是否存在
     */
    public static <T> Result<T> exist() {
        Result<T> result = new Result<>();
        result.setSuccess(false); // 存在该数据
        // 由于vue-element-admin模板在响应数据时验证状态码是否是200，如果不是200，则报错
        result.setCode(ResultCode.SUCCESS); // 执行成功，但存在该数据
        result.setMessage("该数据存在");
        return result;
    }

}
