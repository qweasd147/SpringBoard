package com.joo.api.user.service;

import com.joo.api.login.vo.UserVo;

public interface UserService {

    UserVo getUser(String type, String userId);
}
