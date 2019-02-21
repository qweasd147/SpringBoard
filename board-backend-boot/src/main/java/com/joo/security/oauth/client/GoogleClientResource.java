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
    public UserDto makeUserDto(Map<String, Object> userDetailsMap) {
        List<Map<String, Object>> emails = (List<Map<String, Object>>) userDetailsMap.get("emails");    //TODO : google에선 emails로 넘어오는데, 왜 이렇게 array로 넘겨주는지는 알아봐야함
        Map<String, String> nameInfo = (Map<String, String>)userDetailsMap.get("name");

        String id = userDetailsMap.get("id").toString();
        String name = nameInfo.get("familyName").toString() + nameInfo.get("givenName").toString();
        String nickName = userDetailsMap.get("displayName").toString();

        Map<String, Object> firstEmail = emails.get(0);
        String email = firstEmail.get("value").toString();

        UserDto userDto = new UserDto();

        return userDto.setId(id)
            .setName(name)
            .setNickName(nickName)
            .setEmail(email)
            .setServiceName("google");
    }
}
