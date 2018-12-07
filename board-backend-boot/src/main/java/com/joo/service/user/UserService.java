package com.joo.service.user;


import com.joo.model.dto.UserDto;

public interface UserService {

    /**
     * index로 사용자 정보를 조회한다.
     * @param idx
     * @return
     */
    UserDto getUserByIdx(int idx);

    /**
     * 후보키 정보로 사용자 정보를 조회한다.
     * @param type
     * @param userId
     * @return
     */
    UserDto getUserByCandidate(String type, String userId);

    /**
     * 새로운 회원을 등록한다.
     * @param userVo
     */
    int regist(UserDto userVo);

    /**
     * 회원 정보를 수정한다.
     * @param userVo
     * @return
     */
    int update(UserDto userVo);

    /**
     * 회원 정보를 삭제한다.
     * @param idx
     * @return
     */
    int remove(int idx);
}
