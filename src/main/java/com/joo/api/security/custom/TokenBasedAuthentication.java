package com.joo.api.security.custom;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenBasedAuthentication extends AbstractAuthenticationToken {

    private String token;
    private final UserDetails principle;

    public TokenBasedAuthentication(UserDetails principle) {
        super(principle.getAuthorities());
        this.principle = principle;

    }

    @Override
    public boolean isAuthenticated() {
        //TODO : 일단 모든 권한을 true로 줌
        return true;
        //return super.isAuthenticated();
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principle;
    }
}
