package com.joo.service;

import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.BoardSearchEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {

    /**
     * 게시물 리스트를 조회한다.
     * @param boardSearchEntity
     * @return
     */
    Map selectBoardList(BoardSearchEntity boardSearchEntity, Pageable pageable);

    /**
     * 게시물 리스트 전체 개수를 조회한다.
     * @param boardSearchEntity
     * @return
     */
    int selectBoardListTotCount(BoardSearchEntity boardSearchEntity);

    /**
     * 게시물 하나를 조회한다.
     * @param boardSearchEntity
     * @return
     */
    BoardEntity selectBoardOne(BoardSearchEntity boardSearchEntity);

    /**
     * 게시물 하나를 등록한다.
     * @param boardEntity
     * @param uploadFile
     * @return
     */
    BoardEntity insertBoard(BoardEntity boardEntity, MultipartFile[] uploadFile);

    /**
     * 게시물 하나를 수정한다.
     * @param boardEntity
     * @param uploadFile
     * @param detachFileList
     * @return
     */
    BoardEntity updateBoard(BoardEntity boardEntity, MultipartFile[] uploadFile, List<Integer> detachFileList);

    /**
     * 게시물 하나를 삭제한다.
     * @param boardId
     * @return
     */
    int deleteBoardById(int boardId);
}
