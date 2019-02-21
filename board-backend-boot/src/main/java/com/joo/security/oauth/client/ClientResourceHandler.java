package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import com.joo.security.CustomUserDetails;
import com.joo.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ClientResourceHandler {

    @Autowired
    List<? extends ClientResourceDetails> clientResourceDetails;

    @Autowired
    private TokenUtils tokenUtils;

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    public List<Filter> oauth2Filters(){
        List<Filter> filters = new ArrayList<>();
        clientResourceDetails.forEach(clientResourceDetail-> filters.add(oauth2Filter(clientResourceDetail)));

        return filters;
    }

    private Filter oauth2Filter(ClientResourceDetails clientDetails) {

        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(clientDetails.getLoginRequestPage());
        AuthorizationCodeResourceDetails resource = clientDetails.getClient();
        OAuth2RestTemplate template = new OAuth2RestTemplate(resource, oAuth2ClientContext);

        AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
                new CustomAuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
                new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider()));

        template.setAccessTokenProvider(accessTokenProvider);

        filter.setRestTemplate(template);
        filter.setTokenServices(new UserInfoTokenServices(clientDetails.getResource().getUserInfoUri(), clientDetails.getClient().getClientId()));

        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, Object> userDetailsMap = (Map<String, Object>) ((OAuth2Authentication) authentication).getUserAuthentication().getDetails();

            UserDto userDto = clientDetails.makeUserDto(userDetailsMap);
            CustomUserDetails customUserDetails = new CustomUserDetails(userDto);

            String jwtToken = this.tokenUtils.createToken(customUserDetails);

            response.setHeader("Authorization", jwtToken);
            response.setStatus(HttpStatus.NO_CONTENT.value());
        });
        //TODO : error 페이지 처리
        filter.setAuthenticationFailureHandler((request, response, exception) -> response.sendRedirect("/error"));

        return filter;
    }
}
