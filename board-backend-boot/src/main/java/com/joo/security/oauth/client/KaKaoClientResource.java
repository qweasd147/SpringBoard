package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import org.json.simple.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

public class KaKaoClientResource extends ClientResourceDetails{

    @Override
    public UserDto makeUserDto(Map<String, String> userDetailsMap) {

        String id =userDetailsMap.get("id").toString();	//long 형태로 반환된걸 String으로 변환
        String name = userDetailsMap.get("nickname");
        String nickName = userDetailsMap.get("nickname");
        String email = userDetailsMap.get("kaccount_email");

        UserDto userDto = new UserDto();

        return userDto.setId(id)
                .setName(name)
                .setNickName(nickName)
                .setEmail(email)
                .setServiceName("kakao");
    }
}
