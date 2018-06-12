package com.joo.api.exception;

import org.springframework.validation.BindingResult;

public class ValidateException extends RuntimeException{

    private BindingResult br;
    private Object vo;

    public ValidateException(BindingResult br) {
        this.br = br;
    }

    public ValidateException(BindingResult br, Object vo) {
        this.br = br;
        this.vo = vo;
    }

    public BindingResult getBindResult(){
        return this.br;
    }

    public Object getVo(){
        return this.vo;
    }
}
