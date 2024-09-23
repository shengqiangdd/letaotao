package com.gxcy.letaotao.common.exception;

import com.gxcy.letaotao.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LTException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleLTException(LTException e) { // LTException是自定义异常类
        // 记录日志
        log.error("LTException occurred:", e);
        return Result.error()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage());
    }

    @ExceptionHandler(value = BizException.class)
    public Result<?> handleBizException(BizException e) { // BizException是自定义异常类
        // 记录日志
        log.error("BizException occurred:", e);
        return Result.error()
                .code(e.getErrorCode())
                .message(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 封装错误和提示消息。一般建议为Map；key是字段名，value是错误消息
        Map<String, String> errorMap = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        // 记录日志
        log.error("MethodArgumentNotValidException occurred:", e);
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> { // 遍历错误集合
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            // 返回错误
            return Result.error(errorMap)
                    .code(BizExceptionEnum.PARAM_VALID_ERROR.getCode())
                    .message(BizExceptionEnum.PARAM_VALID_ERROR.getMsg());
        }
        return Result.error()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e) { // Exception是所有异常的父类
        // 记录日志
        log.error("Unexpected error occurred:", e); // 这里会打印异常的堆栈信息到日志中
        return Result.error()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("内部服务器错误：" + e.getMessage());
    }

}