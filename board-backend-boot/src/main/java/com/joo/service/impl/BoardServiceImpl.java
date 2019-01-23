package com.joo.service.impl;

import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.FileDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.model.state.BoardState;
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
        //Page<BoardEntity> boardList = boardRepository.findAllWithFiles(BoardState.ENABLE.getState(),pageable);        //검색 조건을 동적으로 만들 수 없음
        Page<BoardEntity> boardList = boardRepository.findAllDynamic(boardSearchDto, BoardState.ENABLE, pageable);  //결국 query dsl

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
    public BoardDto selectBoardOne(Long boardId) {
        //BoardEntity boardEntity = boardRepository.findById((long) boardId).orElseThrow(() -> new NoSuchElementException("게시글 없음"));
        //boardEntity.setHits(boardEntity.getHits()+1);
        //boardRepository.save(boardEntity);

        boardRepository.incrementHits(boardId);

        return boardRepository.findByIdxAndState(boardId, BoardState.ENABLE.getState())
                .orElseThrow(() -> new NoSuchElementException("게시글 없음")).toDto();
    }

    @Override
    public BoardDto insertBoard(BoardDto boardDto, MultipartFile[] uploadFile) {
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

        //return boardRepository.save(boardDto.toEntity()).toDto();     TODO : 현재 modelMapper를 쓰는데 여기서 순환참조를 제대로 연결 못하고있음....
        return boardRepository.save(boardEntity).toDto();
    }

    @Override
    public BoardDto updateBoard(BoardDto boardDto, MultipartFile[] uploadFile, List<Long> detachFileList) {

        List<FileDto> newFileDtoList = fileUtils.uploadFilesInPhysical(uploadFile);

        List<FileDto> fileDtoFromDB = fileRepository.findByBoardEntity_IdxAndState(boardDto.getIdx(), BoardState.ENABLE.getState())
                .orElse(new ArrayList<>()).stream()
                .map(FileEntity::toDto)
                .map(fileDto -> {
                    //삭제 목록중 있을 시 삭제 flag를 바꾼다.
                    if(detachFileList.contains(boardDto.getIdx())){
                        fileDto.setState(BoardState.DELETE.getState());
                    }
                    return fileDto;
                })
                .collect(Collectors.toList());

        fileDtoFromDB.addAll(newFileDtoList);
        boardDto.setFileList(fileDtoFromDB);

        return boardRepository.save(boardDto.toEntity()).toDto();
    }

    @Override
    public void deleteBoardById(int boardId) {

        BoardEntity boardEntity = boardRepository.findByIdxAndState((long) boardId, BoardState.ENABLE.getState())
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없음"));

        boardEntity.setState(BoardState.DELETE.getState());
        boardRepository.save(boardEntity);
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
