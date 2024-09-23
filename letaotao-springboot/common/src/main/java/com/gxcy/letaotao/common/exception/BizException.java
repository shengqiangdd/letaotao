package com.gxcy.letaotao.common.exception;

import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/10 20:18
 */
@Getter
public class BizException extends RuntimeException {
    private int errorCode;

    public BizException(String message) {
        super(message);
    }

    public BizException(BizExceptionEnum bizExceptionEnum) {
        super(bizExceptionEnum.getMsg());
        this.errorCode = bizExceptionEnum.getCode();
    }

    public BizException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
