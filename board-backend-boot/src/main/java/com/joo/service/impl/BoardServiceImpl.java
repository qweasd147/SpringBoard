package com.joo.service.impl;

import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.BoardSearchEntity;
import com.joo.repository.BoardRepository;
import com.joo.service.BaseService;
import com.joo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl extends BaseService implements BoardService{

    @Autowired
    BoardRepository boardRepository;

    @Override
    public Map selectBoardList(BoardSearchDto boardSearchDto, Pageable pageable) {

        Page<BoardEntity> boardList = boardRepository.findAll(getBoardSpec(boardSearchDto), pageable);

        Map<String, Object> listData = new HashMap<>();

        listData.put("boardList", boardList.getContent());
        listData.put("count", boardList.getTotalElements());    //전체 데이터 수
        listData.put("page", boardList.getNumber());            //현재 페이지

        return listData;
    }

    @Override
    public int selectBoardListTotCount(BoardSearchDto boardSearchDto) {
        return (int) boardRepository.count(getBoardSpec(boardSearchDto));
    }

    @Override
    public BoardDto selectBoardOne(int boardId) {
        BoardEntity boardEntity = boardRepository.findById((long) boardId).orElseThrow(() -> new RuntimeException("못찾음"));
        return boardEntity.toDto();
    }

    @Override
    public BoardDto insertBoard(BoardDto boardDto, MultipartFile[] uploadFile) {
        return boardRepository.save(boardDto.toEntity()).toDto();
    }

    @Override
    public BoardDto updateBoard(BoardDto boardDto, MultipartFile[] uploadFile, List<Integer> detachFileList) {
        return boardRepository.save(boardDto.toEntity()).toDto();
    }

    @Override
    public int deleteBoardById(int boardId) {
        boardRepository.deleteById((long) boardId);
        return 1;
    }

    /**
     * 조회 검색 조건을 반환한다.
     * @param boardSearchDto
     * @return
     */
    private static Specification<BoardEntity> getBoardSpec(BoardSearchDto boardSearchDto){
        return (root, query, cb)->{
            String condition = boardSearchDto.getSearchCondition();  //검색할 column
            String containsLikePattern = getJpaContainsLikePattern(boardSearchDto.getSearchKeyWord());   //검색 키워드

            return cb.or(cb.like(cb.lower(root.get(condition)), containsLikePattern));
        };
    }
}
