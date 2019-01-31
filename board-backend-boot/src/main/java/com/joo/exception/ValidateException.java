package com.joo.exception;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidateException extends RuntimeException{

    private List<Error> errors;
    private Object target;

    public ValidateException(String field, String code, String message){
        this.errors = Collections.singletonList(new Error(field, code, message));
    }

    public ValidateException(String field, String code, String message, Object target){
        this.errors = Collections.singletonList(new Error(field, code, message));
        this.target = target;
    }

    public ValidateException(List<Error> errors){
        this.errors = errors;
    }

    public ValidateException(List<Error> errors, Object target){
        this.errors = errors;
        this.target = target;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public Object getTarget() {
        return target;
    }

    public Map wrapData(){
        HashMap<String, Object> wrapData = new HashMap<>();

        wrapData.put("errors", this.errors);
        wrapData.put("target", this.target);

        return wrapData;
    }

    public static class Error {

        private String field;
        private String code;
        private String message;

        public Error(String field, String code, String message) {
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public String getField() {
            return this.field;
        }

        public String getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
