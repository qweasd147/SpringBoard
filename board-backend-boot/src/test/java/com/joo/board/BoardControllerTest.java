package com.joo.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joo.board.config.SpringSecurityTestConfig;
import com.joo.common.state.CommonState;
import com.joo.config.SecurityConfig;
import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.BoardWriteRequestDto;
import com.joo.model.dto.UserDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.UserEntity;
import com.joo.repository.UserRepository;
import com.joo.security.CustomUserDetails;
import com.joo.security.CustomUserDetailsService;
import com.joo.security.TokenFilter;
import com.joo.security.TokenUtils;
import com.joo.service.BoardService;
import com.joo.service.UserService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;


//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
public class BoardControllerTest extends AbstractControllerTest{

    private static final String BOARD_API = "/api/v1/board";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @InjectMocks
    private BoardController boardController;

    @InjectMocks
    private TokenFilter tokenFilter;

    @Mock
    private BoardService boardService;

    //@MockBean(name="mockUserDetailsService")
    //private CustomUserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final UserDto normalUserDto = UserDto.builder()
            .id("mockID")
            .name("mockName")
            .nickName("mockNickName")
            .email("mockEmail@email.com")
            .serviceName("mockService")
            .state(CommonState.ENABLE)
            .build();

    private final UserDto invalidUserDto = UserDto.builder()
            .id("invalidMockID")
            .name("invalidMockName")
            .nickName("invalidMockNickName")
            .email("mockEmail@email.com")
            .serviceName("invalidMockService")
            .state(CommonState.EXPIRED)
            .build();


    @Override
    public void handleBefore() {
    }


    @Override
    public void handleAfter() {
        //userRepository.deleteById(normalUserDto.getIdx());
        //userRepository.deleteById(invalidUserDto.getIdx());
    }

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

        given(boardService.insertBoard(any())).willReturn(reqDto.toEntity());

        mockMvc.perform(TestUtils.addParamFromDto(multipart(BOARD_API), reqDto)
                //.file(mockFile)
                //.content(OBJECT_MAPPER.writeValueAsString(postReq))
                //.param("subject", "mock를 통한 게시판 제목 입력")
                //.param("contents", "mock를 통한 게시판 내용 입력")
                .characterEncoding("utf-8")
                .headers(getHeaderWithAuthToken()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.subject", is(reqDto.getSubject())))
                .andExpect(jsonPath("$.data.contents", is(reqDto.getContents())));
    }

    @Test
    @DisplayName("만료된 토큰으로 글쓰기 테스트")
    public void registWithExpiredToken() throws Exception {

        BoardWriteRequestDto reqDto = BoardWriteRequestDto.builder()
                .subject("mock을 통한 게시판 제목 입력")
                .contents("mock을 통한 게시판 내용입력")
                .build();

        given(boardService.insertBoard(any())).willReturn(reqDto.toEntity());

        mockMvc.perform(TestUtils.addParamFromDto(multipart(BOARD_API), reqDto)
                .characterEncoding("utf-8").headers(getHeaderWithExpiredToken()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
                //.andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("정상적인 게시물 리스트 검색 테스트")
    public void search() throws Exception {


        BoardSearchDto boardSearchDto = BoardSearchDto.builder()
                .searchCondition("subject")
                .searchKeyWord("name")
                .build();

        String queryStr = TestUtils.dtoToQueryStr(boardSearchDto) + "&pageIdx=1";
        String normalBoardListAPI = BOARD_API + queryStr;
        //Pageable pageable = PageRequest.of(0,10, Sort.Direction.DESC, "idx");

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
