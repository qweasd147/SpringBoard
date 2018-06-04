package com.joo.api.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class BaseController {
    //TODO : Result class 사용 시, 아예 사용 안하거나 크게 바꿔야됨

    protected ResponseEntity successResult(){
        Map<String, Boolean> successMap = new HashMap<>();

        successMap.put("success", Boolean.TRUE);

        return new ResponseEntity<Map>(successMap, HttpStatus.OK);
    }

    protected <T> ResponseEntity successResult(T data){
        return getResult(data, HttpStatus.OK);
    }

    protected ResponseEntity createResult(){
        Map<String, Boolean> successMap = new HashMap<>();

        successMap.put("success", Boolean.TRUE);

        return getResult(successMap, HttpStatus.CREATED);
    }

    protected <T> ResponseEntity createResult(T data){
        return getResult(data, HttpStatus.CREATED);
    }

    private <T> ResponseEntity getResult(T result, HttpStatus statusCode){
        return new ResponseEntity<>(result, statusCode);
    }
}