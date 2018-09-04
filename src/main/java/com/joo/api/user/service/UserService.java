package com.joo.api.user.service;

import com.joo.api.login.vo.UserVo;

public interface UserService {

    /**
     * index로 사용자 정보를 조회한다.
     * @param idx
     * @return
     */
    UserVo getUserByIdx(int idx);

    /**
     * 후보키 정보로 사용자 정보를 조회한다.
     * @param type
     * @param userId
     * @return
     */
    UserVo getUserByCandidate(String type, String userId);

    /**
     * 새로운 회원을 등록한다.
     * @param userVo
     */
    int register(UserVo userVo);

    /**
     * 회원 정보를 수정한다.
     * @param userVo
     * @return
     */
    int update(UserVo userVo);
}
