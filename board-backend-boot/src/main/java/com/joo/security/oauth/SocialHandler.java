package com.joo.security.oauth;

import com.joo.model.dto.UserDto;

import java.util.Map;

public interface SocialHandler {

    UserDto makeUserDto(Map<String, Object> userDetailsMap);
}
