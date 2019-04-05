package com.joo.board;

import com.joo.web.controller.BoardController;
import com.joo.web.controller.common.ControllerExceptionHandler;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class BoardControllerTest {

    @InjectMocks
    private BoardController boardController;

    private MockMvc mockMvc;

    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

}
