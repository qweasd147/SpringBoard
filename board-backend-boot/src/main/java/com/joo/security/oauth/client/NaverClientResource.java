package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties("naver")
@Component
public class NaverClientResource extends ClientResourceDetails{

    public NaverClientResource(@Value("${naver.resource.loginRequestPage}") String loginRequestPage) {
        this.loginRequestPage = loginRequestPage;
    }

    @Override
    public UserDto makeUserDto(Map<String, Object> userDetailsMap, String accessToken) {

        Map<String, String> respMap = (Map<String, String>) userDetailsMap.get("response");

        String id = respMap.get("id");
        String name = respMap.get("name");
        String nickName = respMap.get("nickname");
        String email = respMap.get("email");

        return UserDto.builder()
            .id(id)
            .name(name)
            .nickName(nickName)
            .email(email)
            .thirdPartyToken(accessToken)
            .serviceName("naver")
            .build();
    }
}
