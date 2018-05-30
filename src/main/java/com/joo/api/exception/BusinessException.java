package com.joo.api.exception;

public class BusinessException extends RuntimeException{

    private String code;
    private Object data;

    public BusinessException(String code, String message, Object data) {
        super(message);

        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
