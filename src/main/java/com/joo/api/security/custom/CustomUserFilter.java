package com.joo.api.security.custom;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CustomUserFilter extends UsernamePasswordAuthenticationFilter{

    public static final String TOKEN_HEADER = "X-AUTH-TOKEN";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String authToken = httpRequest.getHeader(this.TOKEN_HEADER);


        chain.doFilter(req, res);
    }
}
