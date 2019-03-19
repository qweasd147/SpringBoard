package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties("kakao")
@Component
public class KaKaoClientResource extends ClientResourceDetails{

    public KaKaoClientResource(@Value("${kakao.resource.loginRequestPage}") String loginRequestPage) {
        this.loginRequestPage = loginRequestPage;
    }

    @Override
    public UserDto makeUserDto(Map<String, Object> userDetailsMap, String accessToken) {

        Map<String, String> properties = (Map<String, String>) userDetailsMap.get("properties");

        String id =userDetailsMap.get("id").toString();
        String name = properties.get("nickname");
        String nickName = properties.get("nickname");
        String email = userDetailsMap.get("kaccount_email").toString();

        return UserDto.builder()
            .id(id)
            .name(name)
            .nickName(nickName)
            .email(email)
            .thirdPartyToken(accessToken)
            .serviceName("kakao")
            .build();
    }
}
