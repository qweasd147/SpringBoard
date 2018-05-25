package com.joo.api.board.service;


import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;

import java.util.Map;

public interface BoardServce {


    Map selectBoardList(BoardSearchVo searchVo);

    int selectBoardListTotCount(BoardSearchVo searchVo);

    BoardVo selectBoardOne(BoardSearchVo searchVo);

    int insertBoard(BoardVo boardVo);

    int updateBoard(BoardVo boardVo);

    int deleteBoardById(int boardId);
}
