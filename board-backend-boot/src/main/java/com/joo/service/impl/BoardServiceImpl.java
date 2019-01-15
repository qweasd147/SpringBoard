package com.joo.service.impl;

import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.FileDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.state.BoardState;
import com.joo.repository.BoardRepository;
import com.joo.service.BaseService;
import com.joo.service.BoardService;
import com.joo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.util.*;

@Service
public class BoardServiceImpl extends BaseService implements BoardService{

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    FileService fileService;

    @Override
    public Map selectBoardList(BoardSearchDto boardSearchDto, Pageable pageable) {

        Page<BoardEntity> boardList = boardRepository.findAll(getBoardSpec(boardSearchDto), pageable);
        //Page<BoardEntity> boardList = boardRepository.findAllWithFiles(BoardState.ENABLE.getState(),pageable);

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
    @Transactional
    public BoardDto selectBoardOne(int boardId) {
        BoardEntity boardEntity = boardRepository.findById((long) boardId).orElseThrow(() -> new RuntimeException("못찾음"));

        //boardEntity.setHits(boardEntity.getHits()+1);
        //boardRepository.save(boardEntity);

        boardRepository.incrementHits((long) boardId);

        return boardEntity.toDto();
    }

    @Override
    public BoardDto insertBoard(BoardDto boardDto, MultipartFile[] uploadFile) {
        List<FileDto> fileDtoList = fileService.uploadFilesInPhysical(uploadFile);

        boardDto.setFileList(fileDtoList);
        fileDtoList.forEach((fileDto)->fileDto.setBoardDto(boardDto));

        return boardRepository.save(boardDto.toEntity()).toDto();
    }

    @Override
    public BoardDto updateBoard(BoardDto boardDto, MultipartFile[] uploadFile, List<Integer> detachFileList) {

        List<FileDto> fileDtoList = fileService.uploadFilesInPhysical(uploadFile);
        boardDto.setFileList(fileDtoList);

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

            final Collection<Predicate> predicates = new ArrayList<>();

            String condition = boardSearchDto.getSearchCondition();  //검색할 column
            String containsLikePattern = getJpaContainsLikePattern(boardSearchDto.getSearchKeyWord());   //검색 키워드

            if(!StringUtils.isEmpty(condition) && !StringUtils.isEmpty(containsLikePattern)){
                predicates.add(cb.like(cb.lower(root.get(condition)), containsLikePattern));
            }

            //return cb.or(cb.like(cb.lower(root.get(condition)), containsLikePattern));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
