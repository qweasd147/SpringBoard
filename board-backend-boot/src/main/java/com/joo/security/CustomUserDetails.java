package com.joo.security;

import com.joo.common.state.CommonState;
import com.joo.model.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class CustomUserDetails extends UserDto implements UserDetails {

    private static final long serialVersionUID = -6180636340827094522L;

    private Set<? extends GrantedAuthority> authorities;

    public CustomUserDetails() {
        super();
    }
    public CustomUserDetails(UserDto userDto) {
        super(userDto);
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /*
    public static UserDtoBuilder builder(UserDto userDto){
        return UserDto.builder()
            .idx(userDto.getIdx())
            .serviceName(userDto.getServiceName())
            .id(userDto.getId())
            .name(userDto.getName())
            .nickName(userDto.getNickName())
            .email(userDto.getEmail())
            .thirdPartyToken(userDto.getThirdPartyToken())
            .state(userDto.getState());
    }
    */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("내가 직접 비밀번호 관리안함");
    }

    @Override
    public String getUsername() {
        return String.valueOf(getIdx());
    }

    @Override
    public boolean isAccountNonExpired() {
        return getState() != CommonState.EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getState() == CommonState.ENABLE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getState() != CommonState.EXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return getState() == CommonState.ENABLE;
    }
}
