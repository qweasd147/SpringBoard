package com.joo.service;

import com.joo.model.BoardSearchVo;
import com.joo.model.entity.Board;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {

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
    Board selectBoardOne(BoardSearchVo searchVo);

    /**
     * 게시물 하나를 등록한다.
     * @param boardVo
     * @param uploadFile
     * @return
     */
    Board insertBoard(Board boardVo, MultipartFile[] uploadFile);

    /**
     * 게시물 하나를 수정한다.
     * @param boardVo
     * @param uploadFile
     * @param detachFileList
     * @return
     */
    Board updateBoard(Board boardVo, MultipartFile[] uploadFile, List<Integer> detachFileList);

    /**
     * 게시물 하나를 삭제한다.
     * @param boardId
     * @return
     */
    int deleteBoardById(int boardId);
}
