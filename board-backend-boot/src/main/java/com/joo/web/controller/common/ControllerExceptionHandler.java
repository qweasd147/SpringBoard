package com.joo.web.controller.common;

import com.joo.exception.BusinessException;
import com.joo.exception.ValidateException;
import com.joo.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Exception 공통 handler
 */
@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler implements BaseController{

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = { SQLException.class })
    public ResponseEntity handleSQLException(SQLException ex){

        logger.error("SQL EXCEPTION!!!");
        logger.error(ex.getMessage());

        printToConsole(ex);

        Result<?> failResult = Result.getFailResult("99", "에러남", null);

        return getRespResult(failResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity handleRuntimeException(RuntimeException ex){

        logger.error("RuntimeException!!!");
        logger.error(ex.getMessage());

        printToConsole(ex);

        Result<?> failResult = Result.getFailResult("99", "에러남", null);

        return getRespResult(failResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { NoSuchElementException.class })
    public ResponseEntity handleNoSuchElementException(RuntimeException ex){

        logger.warn("NoSuchElementException!!!");
        logger.warn(ex.getMessage());

        printToConsole(ex);

        Result<?> failResult = Result.getFailResult("99", "요청 데이터를 찾을 수 없습니다.", null);

        return getRespResult(failResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { BusinessException.class })
    public ResponseEntity handleBusinessException(BusinessException ex){

        logger.error("Business EXCEPTION!!!");
        logger.error(ex.getMessage());

        printToConsole(ex);

        HttpStatus httpStatus = findHttpStatusByCode(ex.getCode());

        Result<?> failResult = Result.getFailResult(ex.getCode(), ex.getMessage(), ex.getData());

        return getRespResult(failResult, httpStatus);
    }

    @ExceptionHandler(value = { ValidateException.class })
    public ResponseEntity handleValidateException(ValidateException ex){

        logger.error("ValidateException EXCEPTION!!!");
        logger.error(ex.getMessage());

        printToConsole(ex);

        BindingResult br = ex.getBindResult();
        List<FieldError> errorList = br.getFieldErrors();

        //debug 이상 레벨이고, list가 존재할 시, 반복문 진행
        if(logger.isDebugEnabled() && !errorList.isEmpty()){
            for(int i=0;i<errorList.size();i++){
                FieldError fieldErr = errorList.get(i);

                logger.debug("필드 : " + fieldErr.getField());
                logger.debug("코드 : " + fieldErr.getCode());
                logger.debug("메세지 : " + fieldErr.getDefaultMessage());
            }
        }

        Result<?> failResult = Result.getFailResult("HTTP_422", "잘못된 요청", ex.getVo());

        return getRespResult(failResult, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private HttpStatus findHttpStatusByCode(String code){

        //상태코드 정보를 따로 안보내줬을 시
        if(!code.startsWith("HTTP_"))     return HttpStatus.INTERNAL_SERVER_ERROR;

        code = code.replaceFirst("HTTP_","");
        int safeHttpCode = Integer.getInteger(code, -1).intValue();

        if(safeHttpCode>100){
            return Arrays.stream(HttpStatus.values())
                    .filter(httpStatus -> httpStatus.value() == safeHttpCode)
                    .findAny()
                    .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private void printToConsole(Throwable throwable){
        if(logger.isDebugEnabled()){
            throwable.printStackTrace();
        }
    }
}
