package com.joo.security.oauth.client;

import com.joo.model.dto.UserDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties("google")
@Component
public class GoogleClientResource extends ClientResourceDetails{

    public GoogleClientResource(@Value("${google.resource.loginRequestPage}") String loginRequestPage) {
        this.loginRequestPage = loginRequestPage;
    }

    @Override
    public UserDto makeUserDto(Map<String, String> userDetailsMap) {
        String id = userDetailsMap.get("id");
        String name = userDetailsMap.get("familyName") + userDetailsMap.get("givenName");
        String nickName = userDetailsMap.get("displayName");


        UserDto userDto = new UserDto();
        return userDto.setId(id)
                .setName(name)
                .setNickName(nickName)
                //.setEmail(email)      //TODO : email 파싱
                .setServiceName("google");
    }
}
