package com.joo.api.security.custom;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 이 어플리케이션에서 직접적으로 관리하지 않음
 */
public class CustomUserFilter extends UsernamePasswordAuthenticationFilter{

    public static final String TOKEN_HEADER = "X-AUTH-TOKEN";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);

        chain.doFilter(req, res);
    }
}
