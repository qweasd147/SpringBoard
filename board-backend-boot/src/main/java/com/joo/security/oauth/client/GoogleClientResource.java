package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("google")
@Component
public class GoogleClientResource extends ClientResourceDetails{

    public GoogleClientResource(@Value("${google.resource.loginRequestPage}") String loginRequestPage) {
        this.loginRequestPage = loginRequestPage;
    }

    @Override
    public UserDto makeUserDto(Map<String, Object> userDetailsMap, String accessToken) {
        String id = userDetailsMap.get("id").toString();
        String name = (String) userDetailsMap.get("name");
        String nickName = userDetailsMap.get("displayName").toString();

        String email = (String) userDetailsMap.get("email");

        return UserDto.builder()
            .id(id)
            .name(name)
            .nickName(nickName)
            .email(email)
            .thirdPartyToken(accessToken)
            .serviceName("google")
            .build();
    }
}
