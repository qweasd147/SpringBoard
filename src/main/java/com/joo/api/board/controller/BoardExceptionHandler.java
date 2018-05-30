package com.joo.api.board.controller;

import com.joo.api.common.Result;
import com.joo.api.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

/**
 * Exception 공통 handler
 */
//TODO : Result class 사용 결정 시, 그에 맞게 보내줘야됨
@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BoardExceptionHandler extends ResponseEntityExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(BoardExceptionHandler.class);

    @ExceptionHandler(value = { SQLException.class })
    public ResponseEntity handleSQLException(SQLException ex){

        logger.error("SQL EXCEPTION!!!");
        logger.error(ex.getMessage());

        Result<?> failResult = Result.getFailResult("99", "에러남", null);

        return new ResponseEntity(failResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { BusinessException.class })
    public ResponseEntity handleBusinessException(BusinessException ex){

        logger.error("Business EXCEPTION!!!");
        logger.error(ex.getMessage());

        Result<?> failResult = Result.getFailResult(ex.getCode(), ex.getMessage(), ex.getData());

        return new ResponseEntity(failResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
