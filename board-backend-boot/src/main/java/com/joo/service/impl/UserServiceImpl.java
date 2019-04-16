package com.joo.service.impl;

import com.joo.model.dto.UserDto;
import com.joo.model.entity.UserEntity;
import com.joo.repository.UserRepository;
import com.joo.service.BaseService;
import com.joo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity insertUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }

    @Override
    public UserEntity insertIfNotExist(UserDto userDto) {

        UserEntity userEntity = userRepository.findByServiceNameAndId(userDto.getServiceName(), userDto.getId());

        if(userEntity == null){
            return userRepository.save(userDto.toEntity());
        }else {
            return userEntity;
        }
    }

    @Override
    public UserEntity update(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }

    @Override
    public void remove(Long userIdx) {
        userRepository.deleteAllByIdInQuery(Arrays.asList(userIdx));
    }

    @Override
    public UserEntity findByIdx(Long idx) {
        return userRepository.findByIdx(idx);
    }
}
