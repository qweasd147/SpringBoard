package com.joo.api.board.service;


import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardServce {

    public static final int SUCCESS_STATE = 1;

    Map selectBoardList(BoardSearchVo searchVo);

    int selectBoardListTotCount(BoardSearchVo searchVo);

    BoardVo selectBoardOne(BoardSearchVo searchVo);

    BoardVo insertBoard(BoardVo boardVo, MultipartFile[] uploadFile);

    BoardVo updateBoard(BoardVo boardVo, MultipartFile[] uploadFile, List<Integer> detachFileList);

    int deleteBoardById(int boardId);
}
