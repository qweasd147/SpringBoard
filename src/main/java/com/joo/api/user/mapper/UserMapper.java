package com.joo.api.user.mapper;

import com.joo.api.login.vo.UserVo;

import java.util.Map;

public interface UserMapper {

    public enum ColumnName{
        serviceName, id, name, nickName, email, state;
    }

    UserVo selectUserOne(Map<String, Object> params);
}
