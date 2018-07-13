package com.joo.api.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 권한이 부족하였을때 로그인 페이지로 보내는게 아니라
 * 그냥 json형태로 보내주기 위해서 만듦
 */
public class CustomEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        HashMap<String,Object> obj = new HashMap<>();
        Long timestamp = System.currentTimeMillis();
        obj.put("path", request.getRequestURI());
        obj.put("timestamp", timestamp);
        obj.put("status", "401");
        obj.put("error", "Unauthorized");
        obj.put("message", "Access Denied");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), obj);
    }
}
