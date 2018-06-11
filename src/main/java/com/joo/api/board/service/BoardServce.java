package com.joo.api.board.service;


import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardServce {

    public static final int SUCCESS_STATE = 1;

    /**
     * 게시물 리스트를 조회한다.
     * @param searchVo
     * @return
     */
    Map selectBoardList(BoardSearchVo searchVo);

    /**
     * 게시물 리스트 전체 개수를 조회한다.
     * @param searchVo
     * @return
     */
    int selectBoardListTotCount(BoardSearchVo searchVo);

    /**
     * 게시물 하나를 조회한다.
     * @param searchVo
     * @return
     */
    BoardVo selectBoardOne(BoardSearchVo searchVo);

    /**
     * 게시물 하나를 등록한다.
     * @param boardVo
     * @param uploadFile
     * @return
     */
    BoardVo insertBoard(BoardVo boardVo, MultipartFile[] uploadFile);

    /**
     * 게시물 하나를 수정한다.
     * @param boardVo
     * @param uploadFile
     * @param detachFileList
     * @return
     */
    BoardVo updateBoard(BoardVo boardVo, MultipartFile[] uploadFile, List<Integer> detachFileList);

    /**
     * 게시물 하나를 삭제한다.
     * @param boardId
     * @return
     */
    int deleteBoardById(int boardId);
}
