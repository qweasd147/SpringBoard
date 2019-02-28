package com.joo.service;

import com.joo.model.dto.UserDto;

public interface UserService {

    UserDto insertUser(UserDto userDto);

    UserDto insertIfNotExist(UserDto userDto);

    UserDto update(UserDto userDto);

    void remove(Long userIdx);

    UserDto findByIdx(Long idx);
}
