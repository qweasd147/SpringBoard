package com.joo.board;

import com.joo.common.state.CommonState;
import com.joo.model.dto.UserDto;
import com.joo.security.CustomUserDetails;
import com.joo.security.TokenUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenUtilsTest {

    //@Autowired
    private TokenUtils tokenUtils;

    @Before
    public void init(){
        tokenUtils = new TokenUtils("abcdefg", 86400);

        //ReflectionTestUtils.setField(tokenUtils,"secret","afsafsafsa");
        //ReflectionTestUtils.setField(tokenUtils,"expiration",86400);
    }

    @Test
    public void a_정상적인_토큰_생성_및_파싱테스트(){

        final UserDto userDto = UserDto.builder()
                .idx(33L)
                .id("mockID")
                .name("mockName")
                .nickName("mockNickName")
                .email("mockEmail@email.com")
                .serviceName("mockService")
                .state(CommonState.ENABLE)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(userDto);
        String enableToken = tokenUtils.createToken(userDetails);

        String userName = tokenUtils.getUsernameFromToken(enableToken);

        assertThat(userName, is("33"));

        Date tokenCreatedDate = tokenUtils.getCreatedDateFromToken(enableToken);
        Date tokenExpirationDate = tokenUtils.getExpirationDateFromToken(enableToken);

        assertTrue(tokenCreatedDate.before(tokenExpirationDate));
    }

    @Test
    public void b_토큰_만료후_테스트(){

        final UserDto userDto = UserDto.builder()
                .idx(33L)
                .id("mockID")
                .name("mockName")
                .nickName("mockNickName")
                .email("mockEmail@email.com")
                .serviceName("mockService")
                .state(CommonState.ENABLE)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(userDto);
        String enableToken = tokenUtils.createToken(userDetails);

        String expiredToken = tokenUtils.deleteToken(enableToken);

        TokenUtils.TokenStatus tokenStatus = tokenUtils.getTokenStatus(expiredToken, userDetails);
        Boolean isEnableToken = tokenUtils.validateToken(expiredToken, userDetails);

        assertFalse(isEnableToken);
        assertThat(tokenStatus, is(TokenUtils.TokenStatus.EXPIRED));
    }

    @Test
    public void c_LOCK_걸린_사용자계정_테스트(){

        final UserDto userDto = UserDto.builder()
                .idx(33L)
                .id("mockID")
                .name("mockName")
                .nickName("mockNickName")
                .email("mockEmail@email.com")
                .serviceName("mockService")
                .state(CommonState.LOCKED)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(userDto);
        String lockedToken = tokenUtils.createToken(userDetails);

        TokenUtils.TokenStatus tokenStatus = tokenUtils.getTokenStatus(lockedToken, userDetails);
        Boolean isEnableToken = tokenUtils.validateToken(lockedToken, userDetails);

        assertFalse(isEnableToken);
        assertThat(tokenStatus, is(TokenUtils.TokenStatus.INVALID));
    }
}
