package com.joo.security;

import com.joo.model.dto.UserDto;
import com.joo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto userDto = userService.findByIdx(Long.parseLong(username));

        if(userDto == null){
            logger.debug("사용자 없음! : "+username);
            throw new UsernameNotFoundException("사용자 없음!");
        }

        CustomUserDetails userDetails = new CustomUserDetails(userDto);

        //TODO : 해당 사용자의 권한 정보 입력
        userDetails.setAuthorities(Collections.EMPTY_SET);

        return userDetails;
    }
}
