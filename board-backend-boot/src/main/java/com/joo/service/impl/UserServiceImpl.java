package com.joo.service.impl;

import com.joo.model.dto.UserDto;
import com.joo.model.entity.UserEntity;
import com.joo.repository.UserRepository;
import com.joo.service.BaseService;
import com.joo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {

    private UserRepository userRepository;

    @Override
    public UserEntity insertUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }

    @Override
    public UserEntity insertIfNotExist(UserDto userDto) {

        UserEntity userEntity = userRepository.findByServiceNameAndId(userDto.getServiceName(), userDto.getId());

        if(userEntity == null){
            UserEntity newUser = userDto.toEntity();
            newUser.initUser();
            return userRepository.save(newUser);
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
