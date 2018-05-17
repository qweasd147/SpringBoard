package com.joo.api.board.controller;

import com.joo.api.board.service.BoardServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class BoardController {

    @Autowired
    BoardServce boardServce;

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public ResponseEntity selectBoard(){

        System.out.println("db testItem");

        boardServce.testCode();

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
