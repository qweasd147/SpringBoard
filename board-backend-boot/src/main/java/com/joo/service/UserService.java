package com.joo.service;

import com.joo.model.dto.UserDto;
import com.joo.model.entity.UserEntity;

public interface UserService {

    UserEntity insertUser(UserDto userDto);

    UserEntity insertIfNotExist(UserDto userDto);

    UserEntity update(UserDto userDto);

    void remove(Long userIdx);

    UserEntity findByIdx(Long idx);
}
