package com.joo.api.login;

import com.joo.api.login.vo.UserVo;

import java.util.Map;

public interface LoginService {
    /**
     * 로그인 정보를 바탕으로 DB에 등록된 정보를 조회한다.
     * @param userVo
     * @return
     */
    Map<String, String> getUser(UserVo userVo);
}
