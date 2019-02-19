package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
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
    public UserDto makeUserDto(Map<String, String> userDetailsMap) {

        String id = userDetailsMap.get("id");
        String name = userDetailsMap.get("name");
        String nickName = userDetailsMap.get("nickname");
        String email = userDetailsMap.get("email");

        UserDto userDto = new UserDto();

        userDto.setId(id)
                .setName(name)
                .setNickName(nickName)
                .setEmail(email)
                .setServiceName("naver");

            return userDto;
    }
}
