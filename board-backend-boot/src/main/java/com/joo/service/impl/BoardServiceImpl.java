package com.joo.service.impl;

import com.joo.common.state.CommonState;
import com.joo.model.dto.*;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.repository.BoardRepository;
import com.joo.repository.FileRepository;
import com.joo.service.BaseService;
import com.joo.service.BoardService;
import com.joo.utils.FileUtils;
import lombok.AllArgsConstructor;
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
@Transactional
@AllArgsConstructor
public class BoardServiceImpl extends BaseService implements BoardService{

    private BoardRepository boardRepository;
    private FileRepository fileRepository;
    private FileUtils fileUtils;

    @Override
    public Page<BoardEntity> selectBoardList(BoardSearchDto boardSearchDto, Pageable pageable) {

        //Page<BoardEntity> boardList1 = boardRepository.findAll(getBoardSpec(boardSearchDto), pageable);                //내가 원하는 컬럼만 추출할수 없음
        Page<BoardEntity> boardList2 = boardRepository.findAllWithFiles(CommonState.ENABLE,pageable);                  //검색 조건을 동적으로 만들 수 없음
        return boardRepository.findAllDynamic(boardSearchDto, CommonState.ENABLE, pageable); //결국 query dsl
    }

    @Override
    public int selectBoardListTotCount(BoardSearchDto boardSearchDto) {
        return (int) boardRepository.count(getBoardSpec(boardSearchDto));
    }

    @Override
    public BoardEntity selectBoardOne(Long boardId) {
        //BoardEntity boardEntity = boardRepository.findById((long) boardId).orElseThrow(() -> new NoSuchElementException("게시글 없음"));
        //boardEntity.setHits(boardEntity.getHits()+1);
        //boardRepository.save(boardEntity);

        boardRepository.incrementHits(boardId);
        return boardRepository.findEnableBoardByBoardIdx(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시글 없음 board idx:"+boardId));
    }

    @Override
    public BoardEntity insertBoard(BoardWriteRequestDto boardWriteRequestDto) {

        MultipartFile[] uploadFiles = boardWriteRequestDto.getUploadFiles().stream().toArray(MultipartFile[]::new);

        List<FileEntity> fileEntities = fileUtils.uploadFilesInPhysical(uploadFiles)
                .stream()
                .map(FileDto::toEntity)
                .collect(Collectors.toList());

        BoardEntity boardEntity = boardWriteRequestDto.toEntity();
        boardEntity.addFiles(fileEntities);

        return boardRepository.save(boardEntity);
    }

    @Override
    public BoardEntity updateBoard(Long boardIdx, BoardUpdateRequestDto boardUpdateRequestDto) {

        BoardEntity boardEntity = boardRepository.findEnableBoardByBoardIdx(boardIdx)
                .orElseThrow(() -> new NoSuchElementException("수정 할 게시글 없음 board idx : " + boardIdx));

        MultipartFile[] uploadFiles = boardUpdateRequestDto.getUploadFiles()
                .stream().toArray(MultipartFile[]::new);
        List<FileEntity> newFileEntityList = fileUtils.uploadFilesInPhysical(uploadFiles)
                .stream().map(FileDto::toEntity).collect(Collectors.toList());

        boardEntity.update(boardUpdateRequestDto.getSubject(), boardUpdateRequestDto.getContents());
        boardEntity.deleteFiles(boardUpdateRequestDto.getDeleteFiles());        //TODO : 삭제 처리가 일괄 처리되는지 확인!
        //fileRepository.deleteAllByIdInQuery(detachFileList);
        boardEntity.addFiles(newFileEntityList);

        //boardRepository.save(boardEntity);
        return boardEntity;
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
