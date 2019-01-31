package com.joo.model.dto;

import com.joo.common.state.CommonState;

import java.io.Serializable;

public class UserDto extends BaseDto<String> implements Serializable{

    private static final long serialVersionUID = 7432896190010419477L;

    private int idx;
    private String serviceName;		//default, kakao, naver 등
    private String id;
    private String name;
    private String nickName;
    private String email;
    private String thirdPartyToken;
    private CommonState state;


    public UserDto() {}

    public UserDto(int idx, String serviceName, String id, String name, String nickName, String email, String thirdPartyToken, CommonState state) {
        this.idx = idx;
        this.serviceName = serviceName;
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.thirdPartyToken = thirdPartyToken;
        this.state = state;
    }

    public UserDto(UserDto userVo) {
        this.idx = userVo.getIdx();
        this.serviceName = userVo.serviceName;
        this.id = userVo.id;
        this.name = userVo.name;
        this.nickName = userVo.nickName;
        this.email = userVo.email;
        this.thirdPartyToken = userVo.thirdPartyToken;
        this.state = userVo.state;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getServiceName() {
        return serviceName;
    }
    public UserDto setServiceName(String serviceName) {
        this.serviceName = serviceName;

        return this;
    }
    public String getId() {
        return id;
    }
    public UserDto setId(String id) {
        this.id = id;

        return this;
    }
    public String getName() {
        return name;
    }
    public UserDto setName(String name) {
        this.name = name;

        return this;
    }
    public String getNickName() {
        return nickName;
    }
    public UserDto setNickName(String nickName) {
        this.nickName = nickName;

        return this;
    }
    public String getEmail() {
        return email;
    }
    public UserDto setEmail(String email) {
        this.email = email;

        return this;
    }

    public String getThirdPartyToken() {
        return thirdPartyToken;
    }

    public void setThirdPartyToken(String thirdPartyToken) {
        this.thirdPartyToken = thirdPartyToken;
    }

    public CommonState getState() {
        return state;
    }

    public UserDto setState(CommonState state) {
        this.state = state;

        return this;
    }

    @Override
    public <T> T toEntity() {
        return null;
    }
}