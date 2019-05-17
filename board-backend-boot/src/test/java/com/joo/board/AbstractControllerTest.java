package com.joo.board;

import com.joo.web.controller.common.ControllerExceptionHandler;
import com.joo.web.controller.common.WebControllerAdvice;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;


public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wc;

    @Autowired
    private Filter springSecurityFilterChain;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders
                //.webAppContextSetup(wc)
                .standaloneSetup(getTargetControllers())
                //.addFilter(springSecurityFilterChain)
                //.apply(springSecurity())
                .setControllerAdvice(new ControllerExceptionHandler())
                .setControllerAdvice(new WebControllerAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                //.setViewResolvers((ViewResolver) (viewName, locale) -> new MappingJackson2JsonView())
                .build();

        handleBefore();
    }

    public abstract void handleBefore();

    @After
    public abstract void handleAfter();

    public abstract Object[] getTargetControllers();
}
