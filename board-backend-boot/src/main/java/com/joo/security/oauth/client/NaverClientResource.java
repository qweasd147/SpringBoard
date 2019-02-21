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
    public UserDto makeUserDto(Map<String, Object> userDetailsMap) {

        Map<String, String> respMap = (Map<String, String>) userDetailsMap.get("response");

        String id = respMap.get("id");
        String name = respMap.get("name");
        String nickName = respMap.get("nickname");
        String email = respMap.get("email");

        UserDto userDto = new UserDto();

        userDto.setId(id)
            .setName(name)
            .setNickName(nickName)
            .setEmail(email)
            .setServiceName("naver");

        return userDto;
    }
}
