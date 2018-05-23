package com.joo.api.board.service;


import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;

import java.util.List;

public interface BoardServce {


    List selectBoardList(BoardSearchVo searchVo);

    int selectBoardListTotCount(BoardSearchVo searchVo);

    BoardVo selectBoardOne(BoardSearchVo searchVo);

    int insertBoard(BoardVo boardVo);

    int updateBoard(BoardVo boardVo);

    int deleteBoardById(int boardId);
}
