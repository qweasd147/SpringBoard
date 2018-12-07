package com.joo.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {

    public static final String EXCEPTION_CODE = "expired_token";
    private Object token;

    public TokenExpiredException(String logMsg, String token) {
        super(logMsg);

        this.token = token;
    }

    public TokenExpiredException(String logMsg) {
        this(logMsg, null);
    }

    public Object getToken() {
        return token;
    }
}
