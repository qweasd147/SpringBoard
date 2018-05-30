package com.joo.api.board.controller;

import com.joo.api.board.service.BoardServce;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import com.joo.api.common.BaseController;
import com.joo.api.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class BoardController extends BaseController {

    @Autowired
    BoardServce boardServce;

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public ResponseEntity selectBoardList(@ModelAttribute BoardSearchVo searchVo){

        Map<String, ?> listData = boardServce.selectBoardList(searchVo);

        return successResult(listData);
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.GET)
    public ResponseEntity selectBoardOne(@ModelAttribute BoardSearchVo searchVo,
                                         @PathVariable int boardId){

        searchVo.setBoardIdx(boardId);

        BoardVo boardOne = boardServce.selectBoardOne(searchVo);

        if(boardOne == null){
            throw new BusinessException("99","데이터가 존재하지 않습니다.", null);
        }

        return successResult(boardOne);
    }

    @RequestMapping(value = "/board", method = RequestMethod.POST)
    public ResponseEntity insertBoard(@ModelAttribute BoardVo boardVo){

        boardServce.insertBoard(boardVo);

        return createResult();
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.POST)
    public ResponseEntity updateBoard(@ModelAttribute BoardVo boardVo, @PathVariable int boardId){

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
