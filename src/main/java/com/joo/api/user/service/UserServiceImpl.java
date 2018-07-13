package com.joo.api.user.service;

import com.joo.api.login.vo.UserVo;
import com.joo.api.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVo getUserByIdx(int idx) {
        return userMapper.getUserByIdx(idx);
    }

    @Override
    public UserVo getUserByCandidate(String type, String userId) {

        Map<String, Object> params = new HashMap<>();

        params.put(UserMapper.ColumnName.SERVICE_NAME.getCamelCase(), type);
        params.put(UserMapper.ColumnName.ID.getCamelCase(), userId);

        return userMapper.getUserByCandidate(params);
    }
}
