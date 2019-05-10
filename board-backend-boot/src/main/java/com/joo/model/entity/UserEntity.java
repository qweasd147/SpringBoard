package com.joo.model.entity;

import com.joo.common.converter.CommonStateImpl;
import com.joo.common.state.CommonState;
import com.joo.model.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public UserEntity(String serviceName, String id, String name, @NotNull String nickName, String email, String thirdPartyToken, CommonState state) {
        this.serviceName = serviceName;
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.thirdPartyToken = thirdPartyToken;
        this.state = state;
    }
}
