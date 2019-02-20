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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ClientResourceHandler {

    /*
    @Autowired
    GoogleClientResource googleClientResource;

    @Autowired
    KaKaoClientResource kaKaoClientResource;

    @Autowired
    NaverClientResource naverClientResource;
    */

    @Autowired
    List<? extends ClientResourceDetails> clientResourceDetails;

    @Autowired
    private TokenUtils tokenUtils;

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    public List<Filter> oauth2Filters(){

        List<Filter> filters = new ArrayList<>();
        /*
        filters.add(oauth2Filter(googleClientResource));
        filters.add(oauth2Filter(kaKaoClientResource));
        filters.add(oauth2Filter(naverClientResource));
        */

        clientResourceDetails.forEach(clientResourceDetail-> filters.add(oauth2Filter(clientResourceDetail)));

        return filters;
    }

    private Filter oauth2Filter(ClientResourceDetails clientDetails) {

        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(clientDetails.getLoginRequestPage());
        OAuth2RestTemplate template = new OAuth2RestTemplate(clientDetails.getClient(), oAuth2ClientContext);

        filter.setRestTemplate(template);
        filter.setTokenServices(new UserInfoTokenServices(clientDetails.getResource().getUserInfoUri(), clientDetails.getClient().getClientId()));

        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, String> userDetailsMap = (Map<String, String>) ((OAuth2Authentication) authentication).getUserAuthentication().getDetails();

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
