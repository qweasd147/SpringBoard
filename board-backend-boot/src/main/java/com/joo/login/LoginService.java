package com.joo.login;

import com.joo.model.dto.UserDto;

import java.util.Map;

public interface LoginService {
    /**
     * 로그인 정보를 바탕으로 DB에 등록된 정보를 조회한다.
     * @param userVo
     * @return
     */
    Map<String, String> getUser(UserDto userVo);
}
