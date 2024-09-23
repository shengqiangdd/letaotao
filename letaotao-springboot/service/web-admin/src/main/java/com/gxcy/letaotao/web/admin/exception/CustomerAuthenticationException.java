package com.gxcy.letaotao.web.admin.exception;

import com.gxcy.letaotao.common.exception.BizExceptionEnum;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证异常雷
 */
@Getter
public class CustomerAuthenticationException extends AuthenticationException {

    private int errorCode;

    public CustomerAuthenticationException(String message) {
        super(message);
    }

    public CustomerAuthenticationException(BizExceptionEnum bizExceptionEnum) {
        super(bizExceptionEnum.getMsg());
        this.errorCode = bizExceptionEnum.getCode();
    }

    public CustomerAuthenticationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
