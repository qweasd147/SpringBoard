package com.joo.api.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joo.api.board.vo.BoardVo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/resources/context/**/context-*.xml"
                        , "file:src/main/webapp/WEB-INF/spring/root-context.xml"
                        , "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
public class BoardTest {

    private static final Logger logger = LoggerFactory.getLogger(BoardTest.class);

    private static final String API_BOARD = "/api/board";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testMain() throws Exception {
        boardInsertTest();          //일반 글쓰기 요청 테스트
        //boardListTest();            //일반 목록 요청 테스트

        //invalidBoardInsertTest();   //벨리데이션 체크용 잘못된 글쓰기 요청 테스트
        //invalidBoardListTest();     //벨리데이션 체크용 잘못된 목록 요청 테스트

    }

    public void boardInsertTest() throws Exception {

        /*
        BoardVo boardVo = new BoardVo();

        boardVo.setSubject("mock를 통한 게시판 제목 입력");
        boardVo.setContents("mock를 통한 게시판 내용 입력");

        String strParameter = mapper.writeValueAsString(boardVo);
        */


        /*  파일 업로드
        File targetFile = new File("파일경로");

        MockMultipartFile mockFile = new MockMultipartFile(
                "saveFileName", targetFile.getName(), null, new FileInputStream(targetFile));
        */


        MvcResult result =
                this.mockMvc.perform(
                        fileUpload(API_BOARD)
                                //.file(mockFile)
                                .param("subject","mock를 통한 게시판 제목 입력")
                                .param("contents","mock를 통한 게시판 내용 입력")
                                //.content(strParameter)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        /*
        logger.info("-------------------------------------------------");
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
        */
    }

    public void boardListTest() throws Exception {

        MvcResult result =
                this.mockMvc.perform(get(API_BOARD))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(model().attributeExists("모델로 보낸 attribute 명"))
                .andReturn();

        /*
        logger.info("-------------------------------------------------");
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
        */
    }

    /**
     * 잘못된 요청값으로 입력 테스트
     * @throws Exception
     */
    public void invalidBoardInsertTest() throws Exception {

        MvcResult result =
                this.mockMvc.perform(fileUpload(API_BOARD))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn();
    }

    /**
     * 잘못된 요청값으로 목록 조회 테스트
     * @throws Exception
     */
    public void invalidBoardListTest() throws Exception {

        MvcResult result =
                this.mockMvc.perform(
                        get(API_BOARD)
                        .param("searchCondition","fsafsafsafsafsafsafsawn")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    //.andExpect(model().attributeExists("모델로 보낸 attribute 명"))
                    .andReturn();
    }
}
