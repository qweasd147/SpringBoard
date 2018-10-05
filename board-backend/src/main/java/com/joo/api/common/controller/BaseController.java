package com.joo.api.common.controller;

import com.joo.api.common.Result;
import com.joo.exception.BusinessException;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 다중 상속을 위해 interface로 만듦...
 * 잘하는 짓인지 모르겠음
 */
public interface BaseController {
    default ResponseEntity successRespResult(){
        return successRespResult(null);
    }

    default <T> ResponseEntity successRespResult(T data){
        return new ResponseEntity<Result>(Result.getSuccessResult(data), HttpStatus.OK);
    }

    default ResponseEntity createRespResult(){
        return createRespResult(null);
    }

    default <T> ResponseEntity createRespResult(T data){
        return new ResponseEntity<Result>(Result.getSuccessResult(data), HttpStatus.CREATED);
    }

    default ResponseEntity warningRespResult(){
        return warningRespResult(null);
    }

    default <T> ResponseEntity warningRespResult(T data){
        return new ResponseEntity<Result>(Result.getWarningResult(data), HttpStatus.OK);
    }

    default ResponseEntity getRespResult(Result result, HttpStatus httpStatus){
        return new ResponseEntity<>(result, httpStatus);
    }

    default ResponseEntity<Resource> getResponseEntityWithResource(String fileName, Resource fileResource){

        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName,"UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("88","파일 다운로드 에러", null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-cache, no-store must-revalidate");
        headers.setPragma("no-cache");
        headers.add("Expiers", "0");
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));
        headers.setContentDispositionFormData(HttpHeaders.CONTENT_DISPOSITION, encodedFileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
                //.contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(fileResource);
    }
}
