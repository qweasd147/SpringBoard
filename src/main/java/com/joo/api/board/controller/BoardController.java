package com.joo.api.board.controller;

import com.joo.api.board.service.BoardServce;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/")
public class BoardController extends BaseController{

    @Autowired
    BoardServce boardServce;

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public ResponseEntity selectBoardList(@ModelAttribute BoardSearchVo searchVo){

        List<?> list = boardServce.selectBoardList(searchVo);

        return successResult(list);
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.GET)
    public ResponseEntity selectBoardOne(@ModelAttribute BoardSearchVo searchVo,
                                         @PathVariable int boardId){

        searchVo.setBoardIdx(boardId);

        BoardVo boardOne = boardServce.selectBoardOne(searchVo);

        return successResult(boardOne);
    }

    @RequestMapping(value = "/board", method = RequestMethod.POST)
    public ResponseEntity insertBoard(@ModelAttribute BoardVo boardVo){

        boardServce.insertBoard(boardVo);

        return createResult();
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.PUT)
    public ResponseEntity updateBoard(@ModelAttribute BoardVo boardVo,
                                      @PathVariable int boardId){

        boardVo.setIdx(boardId);

        boardServce.updateBoard(boardVo);

        return successResult();
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBoard(@PathVariable int boardId){

        boardServce.deleteBoardById(boardId);

        return successResult();
    }
}
