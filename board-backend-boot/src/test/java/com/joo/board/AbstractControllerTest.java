package com.joo.board;

import com.joo.web.controller.common.ControllerExceptionHandler;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wc;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(getTargetControllers())
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

        handleBefore();
    }

    public abstract void handleBefore();

    @After
    public abstract void handleAfter();

    public abstract Object[] getTargetControllers();

}
