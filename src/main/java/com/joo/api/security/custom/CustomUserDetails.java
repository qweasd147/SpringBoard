package com.joo.api.security.custom;

import com.joo.api.login.vo.UserVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class CustomUserDetails extends UserVo implements UserDetails{

    private static final long serialVersionUID = -6180636340827094522L;

    private Set<? extends GrantedAuthority> authorities;

    public CustomUserDetails() {
        super();
    }
    public CustomUserDetails(UserVo userVo) {
        super(userVo);
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

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
        return getState() == State.ENABLED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getState() == State.ENABLED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getState() == State.ENABLED;
    }

    @Override
    public boolean isEnabled() {
        return getState() == State.ENABLED;
    }
}
