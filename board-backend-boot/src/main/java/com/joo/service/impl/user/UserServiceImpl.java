package com.joo.service.impl.user;

import com.joo.model.dto.UserDto;
import com.joo.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto getUserByIdx(int idx) {
        return null;
    }

    @Override
    public UserDto getUserByCandidate(String type, String userId) {
        return null;
    }

    @Override
    public int regist(UserDto userVo) {
        return 0;
    }

    @Override
    public int update(UserDto userVo) {
        return 0;
    }

    @Override
    public int remove(int idx) {
        return 0;
    }
}
