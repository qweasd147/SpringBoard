package com.joo.board.security;

import com.joo.board.TestUtils;
import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardWriteRequestDto;
import com.joo.model.dto.UserDto;
import com.joo.security.CustomUserDetails;
import com.joo.security.CustomUserDetailsService;
import com.joo.security.TokenUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardControllerTestWithSecurity {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenUtils tokenUtils;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    private static final String BOARD_API = "/api/v1/board";
    private static final String DUMMY_TOKEN_VALUE = "abcdefg";

    @Before
    public void init(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void write() throws Exception {

        UserDto normalReqUserDto = UserDto.builder()
                .idx(335L)
                .id("mockID")
                .name("mockName")
                .nickName("mockNickName")
                .email("mockEmail@email.com")
                .serviceName("mockService")
                .state(CommonState.ENABLE)
                .build();

        BoardWriteRequestDto reqDto = BoardWriteRequestDto.builder()
                .subject("mock을 통한 게시판 제목 입력")
                .contents("mock을 통한 게시판 내용입력")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(normalReqUserDto);

        given(customUserDetailsService.loadUserByUsername(any())).willReturn(userDetails);
        given(tokenUtils.getUsernameFromToken((String) any())).willReturn(String.valueOf(normalReqUserDto.getIdx()));
        given(tokenUtils.getTokenStatus(any(), any())).willReturn(TokenUtils.TOKEN_STATUS.ENABLED);


        mockMvc.perform(TestUtils.addParamFromDto(multipart(BOARD_API), reqDto)
                //.file(mockFile)
                //.content(OBJECT_MAPPER.writeValueAsString(postReq))
                //.param("subject", "mock를 통한 게시판 제목 입력")
                //.param("contents", "mock를 통한 게시판 내용 입력")
                .characterEncoding("utf-8").headers(getHeaderWithAuthToken()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.subject", is(reqDto.getSubject())))
                .andExpect(jsonPath("$.data.contents", is(reqDto.getContents())));
    }

    private HttpHeaders getHeaderWithAuthToken(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = "Bearer " + DUMMY_TOKEN_VALUE;

        httpHeaders.add(tokenHeader,token);

        return httpHeaders;
    }
}
