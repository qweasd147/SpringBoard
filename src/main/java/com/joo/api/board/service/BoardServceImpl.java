package com.joo.api.board.service;

import com.joo.api.board.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BoardServceImpl implements BoardServce{

    @Autowired
    BoardMapper boardMapper;

    @Override
    public void testCode() {

        System.out.println("test");

        Map<String, ?> data = boardMapper.testValue();

        System.out.println("data");

    }
}
