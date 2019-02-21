package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import com.joo.security.oauth.SocialHandler;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import java.util.Map;

public abstract class ClientResourceDetails implements SocialHandler {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    protected String loginRequestPage;

    public AuthorizationCodeResourceDetails getClient() {
        return this.client;
    }

    public ResourceServerProperties getResource() {
        return this.resource;
    }

    public String getLoginRequestPage(){
        return this.loginRequestPage;
    }

    @Override
    public abstract UserDto makeUserDto(Map<String, Object> userDetailsMap);
}
