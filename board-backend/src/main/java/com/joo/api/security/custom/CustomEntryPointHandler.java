package com.joo.api.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joo.api.common.Result;
import com.joo.exception.TokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 권한이 부족하였을때 로그인 페이지로 보내는게 아니라
 * 그냥 json형태로 보내주기 위해서 만듦
 */
@Component
public class CustomEntryPointHandler implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomEntryPointHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException{

        logger.debug("login fail : "+request.getRequestURI());

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Result failResult;

        if(authException instanceof TokenExpiredException){
            failResult = Result.getFailResult(TokenExpiredException.EXCEPTION_CODE, "access token 기간 만료", null);
        }else{
            failResult = Result.getFailResult("fail99", "인증 or 권한 부족", null);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), failResult);
    }
}
