package com.joo.model;

public class Result<T> {

    //TODO : 나중에 메세지 리소스 쓰면 그에 맞게 바꿔야됨. 어디까지나 임시용이므로 private
    private enum DefaultCode {
        DEFAULT_SUCCESS_MESSAGE("처리완료")
        , DEFAULT_WARNING_MESSAGE("경고")
        , DEFAULT_FAIL_MESSAGE("처리 실패");

        private String msg;

        DefaultCode(String msg){
            this.msg = msg;
        }
    }

    public enum ResultType {
        SUCCESS, WARNING, FAIL
    }

    private ResultType resultType;
    private String code;
    private String message;
    private T data;

    public Result(ResultType resultType, String code, String message, T data) {
        this.resultType = resultType;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public  static <T> Result<T> getSuccessResult(String code, String msg, T data){
        if(code == null) code = DefaultCode.DEFAULT_SUCCESS_MESSAGE.name();
        if(msg == null) msg = DefaultCode.DEFAULT_SUCCESS_MESSAGE.msg;
        return new Result<>(ResultType.SUCCESS, code, msg, data);
    }

    public  static <T> Result<T> getSuccessResult(T data){
        return getSuccessResult(null, null, data);
    }

    public  static <T> Result<T> getWarningResult(String code, String msg, T data){
        if(code == null) code = DefaultCode.DEFAULT_WARNING_MESSAGE.name();
        if(msg == null) msg = DefaultCode.DEFAULT_WARNING_MESSAGE.msg;
        return new Result<>(ResultType.WARNING, code, msg, data);
    }

    public  static <T> Result<T> getWarningResult(T data){
        return getWarningResult(null, null, data);
    }

    public  static <T> Result<T> getFailResult(String code, String msg, T data){
        if(code == null) code = DefaultCode.DEFAULT_FAIL_MESSAGE.name();
        if(msg == null) msg = DefaultCode.DEFAULT_FAIL_MESSAGE.msg;
        return new Result<>(ResultType.FAIL, code, msg, data);
    }

    public  static <T> Result<T> getFailResult(T data){
        return getFailResult(null, null, data);
    }
}