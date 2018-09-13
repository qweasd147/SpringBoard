package com.joo.api.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joo.api.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
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

        Result failResult = Result.getFailResult("fail99", "권한이 부족함", null);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), failResult);
    }
}
