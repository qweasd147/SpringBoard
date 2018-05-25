package com.joo.api.common;

/**
 * TODO : 서버에서 보내주는 데이터를 정형화하는 목적으로 만든 클래스 인데,
 * 그럼 기존에 front에 개발 된 것도 바꿔야 되서 사용 안할 수도 있음
 * @param <T>
 */
public class Result<T> {

    //TODO : 나중에 메세지 리소스 쓰면 그에 맞게 바꿔야됨. 어디까지나 임시용이므로 private
    private static final String DEFAULT_SUCCESS_MESSAGE = "처리 완료";
    private static final String DEFAULT_WARNING_MESSAGE = "경고";
    private static final String DEFAULT_FAIL_MESSAGE = "처리 실패";

    public enum ResultType {
        SUCCESS, WARNING, FAIL
    }

    private ResultType resultType;
    private String message;
    private String code;
    private T data;

    public Result(ResultType resultType, String message, String code, T data) {
        this.resultType = resultType;
        this.message = message;
        this.code = code;
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
        if(msg == null) msg = DEFAULT_SUCCESS_MESSAGE;
        return new Result<>(ResultType.SUCCESS, msg, code, data);
    }

    public  static <T> Result<T> getWarningResult(String code, String msg, T data){
        if(msg == null) msg = DEFAULT_WARNING_MESSAGE;
        return new Result<>(ResultType.WARNING, msg, code, data);
    }

    public  static <T> Result<T> getFailResult(String code, String msg, T data){
        if(msg == null) msg = DEFAULT_FAIL_MESSAGE;
        return new Result<>(ResultType.FAIL, msg, code, data);
    }
}
