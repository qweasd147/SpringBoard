package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;

import java.util.Map;

public class NaverClientResource extends ClientResourceDetails{

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
