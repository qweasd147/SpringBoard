package com.joo.service.impl;

import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.FileDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.repository.BoardRepository;
import com.joo.repository.FileRepository;
import com.joo.service.BaseService;
import com.joo.service.BoardService;
import com.joo.utils.FileUtils;
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
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl extends BaseService implements BoardService{

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileUtils fileUtils;

    @Override
    public Map selectBoardList(BoardSearchDto boardSearchDto, Pageable pageable) {

        //Page<BoardEntity> boardList = boardRepository.findAll(getBoardSpec(boardSearchDto), pageable);                //내가 원하는 컬럼만 추출할수 없음
        //Page<BoardEntity> boardList = boardRepository.findAllWithFiles(CommonState.ENABLE,pageable);                  //검색 조건을 동적으로 만들 수 없음
        Page<BoardEntity> boardList = boardRepository.findAllDynamic(boardSearchDto, CommonState.ENABLE, pageable); //결국 query dsl

        Map<String, Object> listData = new HashMap<>();

        listData.put("boardList", boardList.getContent());
        listData.put("count", boardList.getTotalElements());    //전체 데이터 수
        listData.put("page", boardList.getNumber()+1);          //현재 페이지

        return listData;
    }

    @Override
    public int selectBoardListTotCount(BoardSearchDto boardSearchDto) {
        return (int) boardRepository.count(getBoardSpec(boardSearchDto));
    }

    @Override
    @Transactional
    public BoardEntity selectBoardOne(Long boardId) {
        //BoardEntity boardEntity = boardRepository.findById((long) boardId).orElseThrow(() -> new NoSuchElementException("게시글 없음"));
        //boardEntity.setHits(boardEntity.getHits()+1);
        //boardRepository.save(boardEntity);

        boardRepository.incrementHits(boardId);
        return boardRepository.findEnableBoardByBoardIdx(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시글 없음 board idx:"+boardId));
    }

    @Override
    public BoardEntity insertBoard(BoardDto boardDto, MultipartFile[] uploadFile) {
        //TODO : 순환 참조 로직을 어디다 둘 지 고민중
        /*
        List<FileDto> fileDtoList = fileUtils.uploadFilesInPhysical(uploadFile);

        BoardEntity boardEntity = boardDto.toEntity();
        List<FileEntity> fileEntityList = fileDtoList
                .stream()
                .map(FileDto::toEntity)
                .collect(Collectors.toList());

        fileEntityList.forEach(fileEntity -> fileEntity.setBoardEntity(boardEntity));
        boardEntity.setFileList(fileEntityList);

        boardDto.setFileList(fileDtoList);
        fileDtoList.forEach((fileDto)->fileDto.setBoardDto(boardDto));
        return boardRepository.save(boardEntity).toDto();
        */

        List<FileEntity> fileEntities = fileUtils.uploadFilesInPhysical(uploadFile)
                .stream()
                .map(FileDto::toEntity)
                .collect(Collectors.toList());

        BoardEntity boardEntity = boardDto.toEntity();
        boardEntity = boardEntity.toBuilder().state(CommonState.ENABLE).build();
        boardEntity.addFiles(fileEntities);

        return boardRepository.save(boardEntity);
    }

    @Override
    public BoardEntity updateBoard(BoardDto boardDto, MultipartFile[] uploadFile, List<Long> detachFileList) {

        List<FileEntity> newFileEntityList = fileUtils.uploadFilesInPhysical(uploadFile)
                .stream().map(FileDto::toEntity).collect(Collectors.toList());

        BoardEntity boardEntity = boardDto.toEntity();

        boardEntity.deleteFiles(detachFileList);        //TODO : 삭제 처리가 일괄 처리되는지 확인!
        //fileRepository.deleteAllByIdInQuery(detachFileList);
        boardEntity.addFiles(newFileEntityList);

        //boardRepository.save(boardEntity);
        return boardRepository.findEnableBoardByBoardIdx(boardEntity.getIdx())
                .orElseThrow(() -> new NoSuchElementException("게시글 없음 board idx:"+boardEntity.getIdx()));
    }

    @Override
    public void deleteBoardById(Long boardId) {
        BoardEntity boardEntity = boardRepository.findEnableBoardByBoardIdx(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시글 없음 board idx:"+boardId));

        boardEntity.delete();
        //boardRepository.save(boardEntity);
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
