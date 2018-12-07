package com.joo.service.impl;

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
    public Map selectBoardList(BoardSearchEntity boardSearchEntity, Pageable pageable) {

        Page<BoardEntity> boardList = boardRepository.findAll(getBoardSpec(boardSearchEntity), pageable);

        Map<String, Object> listData = new HashMap<>();

        listData.put("boardList", boardList.getContent());
        listData.put("count", boardList.getTotalElements());    //전체 데이터 수
        listData.put("page", boardList.getNumber());            //현재 페이지

        return listData;
    }

    @Override
    public int selectBoardListTotCount(BoardSearchEntity boardSearchEntity) {
        return (int) boardRepository.count(getBoardSpec(boardSearchEntity));
    }

    @Override
    public BoardEntity selectBoardOne(BoardSearchEntity boardSearchEntity) {
        return boardRepository.findById((long) boardSearchEntity.getBoardIdx()).orElseThrow(()->new RuntimeException("못찾음"));
    }

    @Override
    public BoardEntity insertBoard(BoardEntity boardEntity, MultipartFile[] uploadFile) {
        return boardRepository.save(boardEntity);
    }

    @Override
    public BoardEntity updateBoard(BoardEntity boardVo, MultipartFile[] uploadFile, List<Integer> detachFileList) {
        return boardRepository.save(boardVo);
    }

    @Override
    public int deleteBoardById(int boardId) {
        boardRepository.deleteById((long) boardId);
        return 1;
    }

    /**
     * 조회 검색 조건을 반환한다.
     * @param boardSearchEntity
     * @return
     */
    private static Specification<BoardEntity> getBoardSpec(BoardSearchEntity boardSearchEntity){
        return (root, query, cb)->{
            String condition = boardSearchEntity.getSearchCondition();  //검색할 column
            String containsLikePattern = getJpaContainsLikePattern(boardSearchEntity.getSearchKeyWord());   //검색 키워드

            return cb.or(cb.like(cb.lower(root.get(condition)), containsLikePattern));
        };
    }
}
