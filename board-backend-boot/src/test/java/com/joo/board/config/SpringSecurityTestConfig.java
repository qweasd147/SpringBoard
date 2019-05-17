package com.joo.board.config;

import com.joo.common.state.CommonState;
import com.joo.model.dto.UserDto;
import com.joo.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
@Slf4j
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {


        UserDto normalReqUserDto = UserDto.builder()
                .idx(332L)
                .id("mockID")
                .name("mockName")
                .nickName("mockNickName")
                .email("mockEmail@email.com")
                .serviceName("mockService")
                .state(CommonState.ENABLE)
                .build();

        UserDto invalidReqUserDto = UserDto.builder()
                .idx(335L)
                .id("invalidMockID")
                .name("invalidMockName")
                .nickName("invalidMockNickName")
                .email("mockEmail@email.com")
                .serviceName("invalidMockService")
                .state(CommonState.EXPIRED)
                .build();

        MockCustomUserDetails mockCustomUser1 = new MockCustomUserDetails(normalReqUserDto);
        MockCustomUserDetails mockCustomUser2 = new MockCustomUserDetails(invalidReqUserDto);

        return new InMemoryUserDetailsManager(Arrays.asList(mockCustomUser1, mockCustomUser2));
    }


    /**
     * CustomUserDetails에선 password를 관리 안하고 exception을 던지는데
     * 테스트 내부에선 password를 접근해서 그 부분만 수정한 형태
     */
    public static class MockCustomUserDetails extends CustomUserDetails{

        public MockCustomUserDetails(UserDto userDto) {
            super(userDto);
        }

        @Override
        public String getPassword() {
            log.debug("access getPassword Method in MockCustomUserDetails.class");
            return null;
        }
    }
}
