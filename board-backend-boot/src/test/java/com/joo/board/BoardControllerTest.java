package com.joo.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardDto;
import com.joo.model.dto.UserDto;
import com.joo.security.CustomUserDetails;
import com.joo.security.TokenUtils;
import com.joo.service.BoardService;
import com.joo.web.controller.BoardController;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;


//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardControllerTest extends AbstractControllerTest{

    private static final String BOARD_API = "/api/v1/board";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    private UserDto normalUserDto;
    private UserDto invalidUserDto;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public void handleBefore() {

        normalUserDto = UserDto.builder()
            .idx(-99L)
            .id("mockID")
            .name("mockName")
            .nickName("mockNickName")
            .serviceName("mockService")
            .state(CommonState.ENABLE)
            .build();

        invalidUserDto = UserDto.builder()
            .idx(-98L)
            .id("invalidMockID")
            .name("invalidMockName")
            .nickName("invalidMockNickName")
            .serviceName("invalidMockService")
            .state(CommonState.EXPIRED)
            .build();
    }

    @Override
    public void handleAfter() {

    }

    @Override
    public Object[] getTargetControllers() {
        return new Object[]{boardController};
    }

    @Test
    @DisplayName("정상적인 글쓰기 테스트")
    public void regist() throws Exception {
        BoardDto boardDto = BoardDto.builder()
            .subject("mock를 통한 게시판 제목 입력")
            .contents("mock를 통한 게시판 내용 입력")
            .build();

        ResultActions resultActions = requestRegistWithAuthToken(boardDto);

        resultActions
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("정상적인 게시물 리스트 검색 테스트")
    public void search() throws Exception {

        String normalBoardListAPI = BOARD_API + "?searchCondition=subject&searchKeyWord=name";

        MvcResult result =
            this.mockMvc.perform(get(normalBoardListAPI))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(model().attributeExists("모델로 보낸 attribute 명"))
                .andReturn();

    }


    private ResultActions requestRegistWithAuthToken(BoardDto boardDto) throws Exception {
        return mockMvc.perform(multipart(BOARD_API)
                //.file(mockFile)
                //.content(OBJECT_MAPPER.writeValueAsString(postReq))
                .param("subject", "mock를 통한 게시판 제목 입력")
                .param("contents", "mock를 통한 게시판 내용 입력")
                .characterEncoding("utf-8").headers(getHeaderWithAuthToken()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /**
     * 정상 토큰을 담고 있는 헤더
     * @return
     */
    private HttpHeaders getHeaderWithAuthToken(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = "Bearer " + createToken(normalUserDto);

        httpHeaders.add(tokenHeader,token);

        return httpHeaders;
    }

    /**
     * 삭제된 사용자 정보를 가지고 있는 토큰을 담고 있는 헤더
     * @return
     */
    private HttpHeaders getHeaderWithInvalidAuthToken(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = "Bearer " + createToken(invalidUserDto);

        httpHeaders.add(tokenHeader,token);

        return httpHeaders;
    }

    /**
     * 만료된 토큰을 담고 있는 헤더
     * @return
     */
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

}
