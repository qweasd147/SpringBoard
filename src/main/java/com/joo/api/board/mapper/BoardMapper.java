package com.joo.api.board.mapper;

import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "boardMapper")
public interface BoardMapper {

    /**
     * 조건에 맞는 게시글 목록을 조회한다.
     * @param searchVo
     * @return
     */
    List<BoardVo> selectBoardList(BoardSearchVo searchVo);

    /**
     * 전체 게시글 목록 갯수를 조회한다.
     * @param searchVo
     * @return
     */
    int selectBoardListTotCount(BoardSearchVo searchVo);

    /**
     * 조건에 맞는 게시물 한건을 조회한다.
     * @param boardVo
     * @return
     */
    BoardVo selectBoardOne(BoardSearchVo boardVo);

    /**
     * 게시물 한건을 등록한다.
     * @param boardVo
     * @return
     */
    void insertBoard(BoardVo boardVo);

    /**
     * 게시물 한건을 수정한다.
     * @param boardVo
     * @return
     */
    void updateBoard(BoardVo boardVo);

    /**
     * 게시물 한건을 삭제한다.
     * @param boardId
     * @return
     */
    void deleteBoardById(int boardId);

    /**
     * 게시판 조회수를 증가 시킨다.
     * @param boardId
     * @return
     */
    void updateBoardHits(int boardId);
}
