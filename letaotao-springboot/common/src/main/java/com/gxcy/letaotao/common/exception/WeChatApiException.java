package com.gxcy.letaotao.common.exception;

public class WeChatApiException extends RuntimeException {
    private String errorMessage; // 错误信息
    private int errorCode; // 错误码

    public WeChatApiException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public WeChatApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    public WeChatApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // getters and setters
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
