package com.joo.model.dto;

import com.joo.common.state.CommonState;
import com.joo.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class UserDto extends BaseDto<String> implements Serializable{

    private static final long serialVersionUID = 7432896190010419477L;

    private Long idx;
    private String serviceName;		//default, kakao, naver 등
    private String id;
    private String name;
    private String nickName;
    private String email;
    private String thirdPartyToken;
    private CommonState state;

    @Builder(toBuilder = true)
    public UserDto(Long idx, String serviceName, String id, String name, String nickName, String email, String thirdPartyToken, CommonState state) {
        this.idx = idx;
        this.serviceName = serviceName;
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.thirdPartyToken = thirdPartyToken;
        this.state = state;
    }

    public UserDto(UserDto userDto) {
        this.idx = userDto.getIdx();
        this.serviceName = userDto.serviceName;
        this.id = userDto.id;
        this.name = userDto.name;
        this.nickName = userDto.nickName;
        this.email = userDto.email;
        this.thirdPartyToken = userDto.thirdPartyToken;
        this.state = userDto.state;
    }

    @Override
    public UserEntity toEntity() {
        return convertType(this, UserEntity.class);
    }
}