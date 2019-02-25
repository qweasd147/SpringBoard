package com.joo.service.impl;

import com.joo.model.dto.UserDto;
import com.joo.model.entity.UserEntity;
import com.joo.repository.UserRepository;
import com.joo.service.BaseService;
import com.joo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto insertUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity()).toDto();
    }

    @Override
    public UserDto insertIfNotExist(UserDto userDto) {

        UserEntity userEntity = userRepository.findByServiceNameAndId(userDto.getServiceName(), userDto.getId());

        if(userEntity == null){
            return userRepository.save(userDto.toEntity()).toDto();
        }else {
            return userEntity.toDto();
        }
    }

    @Override
    public UserDto update(UserDto userDto) {
        return userRepository.save(userDto.toEntity()).toDto();
    }

    @Override
    public void remove(Long userIdx) {
        userRepository.deleteAllByIdInQuery(Arrays.asList(userIdx));
    }
}
