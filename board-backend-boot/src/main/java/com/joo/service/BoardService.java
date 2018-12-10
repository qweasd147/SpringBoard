package com.joo.service;

import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {

    /**
     * 게시물 리스트를 조회한다.
     * @param boardSearchDto
     * @return
     */
    Map selectBoardList(BoardSearchDto boardSearchDto, Pageable pageable);

    /**
     * 게시물 리스트 전체 개수를 조회한다.
     * @param boardSearchDto
     * @return
     */
    int selectBoardListTotCount(BoardSearchDto boardSearchDto);

    /**
     * 게시물 하나를 조회한다.
     * @param boardSearchDto
     * @return
     */
    BoardDto selectBoardOne(BoardSearchDto boardSearchDto);

    /**
     * 게시물 하나를 등록한다.
     * @param boardDto
     * @param uploadFiles
     * @return
     */
    BoardDto insertBoard(BoardDto boardDto, MultipartFile[] uploadFiles);

    /**
     * 게시물 하나를 수정한다.
     * @param boardDto
     * @param uploadFiles
     * @param detachFileList
     * @return
     */
    BoardDto updateBoard(BoardDto boardDto, MultipartFile[] uploadFiles, List<Integer> detachFileList);

    /**
     * 게시물 하나를 삭제한다.
     * @param boardId
     * @return
     */
    int deleteBoardById(int boardId);
}
