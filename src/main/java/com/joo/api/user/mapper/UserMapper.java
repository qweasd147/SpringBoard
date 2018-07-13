package com.joo.api.user.mapper;

import com.joo.api.login.vo.UserVo;

import java.util.Map;

public interface UserMapper {

    public enum ColumnName{
        SERVICE_NAME("serviceName"), ID("id")
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
}
