package com.joo.api.exception;

public class BusinessException extends RuntimeException{

    private String code;
    private Object data;

    public BusinessException(String code, String logMsg, Object data) {
        super(logMsg);

        this.code = code;
        this.data = data;
    }

    public BusinessException(String code, String logMsg) {
        this(code, logMsg, null);
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
