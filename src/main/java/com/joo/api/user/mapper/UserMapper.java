package com.joo.api.user.mapper;

import com.joo.api.login.vo.UserVo;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserMapper {

    public enum ColumnName{
        IDX("idx"), SERVICE_NAME("serviceName"), ID("id")
        , NAME("name"), NICK_NAME("nickName"), EMAIL("email"), STATE("state");

        private String camelCase;

        ColumnName(String camelCase) {
            this.camelCase = camelCase;
        }

        public String getCamelCase() {
            return this.camelCase;
        }
    }

    /**
     * index값으로 사용자 정보를 조회한다.
     * @param idx
     * @return
     */
    UserVo getUserByIdx(int idx);

    /**
     * 후보키 정보로 사용자 정보를 조회한다.
     * @param params
     * @return
     */
    UserVo getUserByCandidate(Map<String, Object> params);

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

    /**
     * 회원 정보를 DB에서 완전히 삭제한다.
     * @param idx
     * @return
     */
    int deleteFromDB(int idx);
}
