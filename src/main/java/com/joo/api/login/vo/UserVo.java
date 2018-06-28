package com.joo.api.login.vo;

import java.io.Serializable;

public class UserVo implements Serializable{


    public enum State{
        //TODO : enable 여부만 체크
        ENABLED(0), LOCKED(1), EXPIRED(2);

        private int state;
        State(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    private static final long serialVersionUID = 7432896190010419477L;

    private String serviceName;		//default, kakao, naver 등
    private String id;
    private String name;
    private String nickName;
    private String email;
    private String accessToken;
    private State state;


    public UserVo() {}

    public UserVo(String serviceName, String id, String name, String nickName, String email, String accessToken, State state) {
        this.serviceName = serviceName;
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.accessToken = accessToken;
        this.state = state;
    }

    public UserVo(UserVo userVo) {
        this.serviceName = userVo.serviceName;
        this.id = userVo.id;
        this.name = userVo.name;
        this.nickName = userVo.nickName;
        this.email = userVo.email;
        this.accessToken = userVo.accessToken;
        this.state = userVo.state;
    }

    public String getServiceName() {
        return serviceName;
    }
    public UserVo setServiceName(String serviceName) {
        this.serviceName = serviceName;

        return this;
    }
    public String getId() {
        return id;
    }
    public UserVo setId(String id) {
        this.id = id;

        return this;
    }
    public String getName() {
        return name;
    }
    public UserVo setName(String name) {
        this.name = name;

        return this;
    }
    public String getNickName() {
        return nickName;
    }
    public UserVo setNickName(String nickName) {
        this.nickName = nickName;

        return this;
    }
    public String getEmail() {
        return email;
    }
    public UserVo setEmail(String email) {
        this.email = email;

        return this;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public UserVo setAccessToken(String accessToken) {
        this.accessToken = accessToken;

        return this;
    }

    public State getState() {
        return state;
    }

    public UserVo setState(State state) {
        this.state = state;

        return this;
    }
}