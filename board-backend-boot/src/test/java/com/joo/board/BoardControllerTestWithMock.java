package com.joo.board;

import com.google.common.collect.ImmutableList;
import com.joo.board.config.SpringSecurityTestConfig;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.BoardWriteRequestDto;
import com.joo.service.BoardService;
import com.joo.web.controller.BoardController;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 테스트 대상 : security를 제외한 controller 로직.
 * mock을 사용하여 진짜 service, repository 등은 테스트 하지 않고 순수 controller만 테스트
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
public class BoardControllerTestWithMock extends AbstractControllerTest{

    private static final String BOARD_API = "/api/v1/board";

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

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
                .characterEncoding("utf-8"))
                .andDo(print())
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
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /*
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
    */

    @Test
    @DisplayName("정상적인 게시물 리스트 검색 테스트")
    public void search() throws Exception {


        BoardSearchDto boardSearchDto = BoardSearchDto.builder()
                .searchCondition("subject")
                .searchKeyWord("name")
                .build();

        String queryStr = TestUtils.dtoToQueryStr(boardSearchDto, ImmutableList.of("boardIdx")) + "&pageIdx=1";
        String normalBoardListAPI = BOARD_API + queryStr;

        this.mockMvc.perform(get(normalBoardListAPI))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(model().attributeExists("모델로 보낸 attribute 명"))
                .andReturn();
    }
}
