package com.gxcy.letaotao.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LTException extends RuntimeException {

    public LTException(String message, Throwable cause) {
        super(message, cause);
    }

    public LTException(String message) {
        super(message);
    }
}
