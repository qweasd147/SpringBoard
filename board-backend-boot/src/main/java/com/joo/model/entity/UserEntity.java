package com.joo.model.entity;

import com.joo.common.converter.CommonStateImpl;
import com.joo.common.state.CommonState;
import com.joo.model.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class UserEntity extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    @NotNull
    private String serviceName;		//default, kakao, naver 등

    /**
     * 각 서드파티에서 어떠한 형태로 제공할지 몰라서 String
     */
    @Column(nullable = false)
    @NotNull
    private String id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private String nickName;

    @Column(nullable = false)
    @NotNull
    private String email;

    @Transient
    private String thirdPartyToken;

    @Convert(converter = CommonStateImpl.class)
    private CommonState state;

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setState(CommonState state) {
        this.state = state;
    }

    @Override
    public UserDto toDto() {
        return convertType(this, UserDto.class);
    }
}
