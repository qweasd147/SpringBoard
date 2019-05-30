package com.joo.board;

import com.google.common.collect.ImmutableList;
import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.BoardWriteRequestDto;
import com.joo.model.dto.UserDto;
import com.joo.security.CustomUserDetails;
import com.joo.web.controller.BoardController;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;

/**
 * 테스트 대상 : security를 제외한 BoardController
 *
 * service랑 repository가 mock으로 주입한게 아니라 실제하는거 사용.
 * 대신 transaction annotation으로 commit이 되지 않는다(rollback).
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringSecurityTestConfig.class)
@SpringBootTest
@Transactional
public class BoardControllerTest extends AbstractControllerTest{

    private static final String BOARD_API = "/api/v1/board";

    @Autowired
    private BoardController boardController;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public void handleBefore() {}


    @Override
    public void handleAfter() {}

    @Override
    public Object[] getTargetControllers() {
        return new Object[]{boardController};
    }

    @Test
    @DisplayName("정상적인 글쓰기 테스트")
    public void regist() throws Exception {

        BoardWriteRequestDto reqDto = BoardWriteRequestDto.builder()
                .subject("mock을 통한 게시판 제목 입력")
                .contents("mock을 통한 게시판 내용입력")
                .build();

        setAuthInContext();

        mockMvc.perform(TestUtils.addParamFromDto(multipart(BOARD_API), reqDto)
                //.file(mockFile)
                //.content(OBJECT_MAPPER.writeValueAsString(postReq))
                //.param("subject", "mock를 통한 게시판 제목 입력")
                //.param("contents", "mock를 통한 게시판 내용 입력")
                .characterEncoding("utf-8")
                //.headers(getHeaderWithAuthToken()))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.subject", is(reqDto.getSubject())))
                .andExpect(jsonPath("$.data.contents", is(reqDto.getContents())));
    }

    @Test
    @DisplayName("필수값이 없을때 글쓰기 테스트")
    public void regist_필수값_누락() throws Exception {

        BoardWriteRequestDto reqDto = BoardWriteRequestDto.builder()
                .subject("mock을 통한 게시판 제목 입력")
                //.contents("mock을 통한 게시판 내용입력")
                .build();

        //given(boardService.insertBoard(any())).willReturn(reqDto.toEntity());

        mockMvc.perform(TestUtils.addParamFromDto(multipart(BOARD_API), reqDto)
                //.file(mockFile)
                //.content(OBJECT_MAPPER.writeValueAsString(postReq))
                //.param("subject", "mock를 통한 게시판 제목 입력")
                //.param("contents", "mock를 통한 게시판 내용 입력")
                .characterEncoding("utf-8")
                //.headers(getHeaderWithAuthToken()))
                ).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("정상적인 게시물 리스트 검색 테스트")
    public void search() throws Exception {


        BoardSearchDto boardSearchDto = BoardSearchDto.builder()
                .searchCondition("subject")
                .searchKeyWord("name")
                .build();

        String queryStr = TestUtils.dtoToQueryStr(boardSearchDto, ImmutableList.of("boardIdx")) + "&pageIdx=1";
        String normalBoardListAPI = BOARD_API + queryStr;
        //Pageable pageable = PageRequest.of(0,10, Sort.Direction.DESC, "idx");

        this.mockMvc.perform(get(normalBoardListAPI))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(model().attributeExists("모델로 보낸 attribute 명"))
                .andReturn();
    }

    /*
    private HttpHeaders getHeaderWithAuthToken(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = "Bearer " + createToken(normalUserDto);

        httpHeaders.add(tokenHeader,token);

        return httpHeaders;
    }

    private HttpHeaders getHeaderWithInvalidAuthToken(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = "Bearer " + createToken(invalidUserDto);

        httpHeaders.add(tokenHeader,token);

        return httpHeaders;
    }

    private HttpHeaders getHeaderWithExpiredToken(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = createToken(normalUserDto);

        //토큰 삭제 시, 만료된 토큰을 반환
        token = "Bearer " + this.tokenUtils.deleteToken(token);

        httpHeaders.add(tokenHeader,token);

        return httpHeaders;
    }

    private String createToken(UserDto userDto){
        CustomUserDetails customUserDetails = new CustomUserDetails(userDto);
        return this.tokenUtils.createToken(customUserDetails);
    }
    */

    private void setAuthInContext(){

        final UserDto normalUserDto = UserDto.builder()
                .id("mockID")
                .name("mockName")
                .nickName("mockNickName")
                .email("mockEmail@email.com")
                .serviceName("mockService")
                .state(CommonState.ENABLE)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(normalUserDto);
        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
