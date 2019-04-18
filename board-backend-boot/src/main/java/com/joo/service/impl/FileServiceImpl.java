package com.joo.service.impl;

import com.joo.common.state.CommonState;
import com.joo.model.entity.FileEntity;
import com.joo.repository.BoardRepository;
import com.joo.repository.FileRepository;
import com.joo.service.BaseService;
import com.joo.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class FileServiceImpl extends BaseService implements FileService{

    private FileRepository fileRepository;
    private BoardRepository boardRepository;

    @Override
    public FileEntity selectFileOne(Long boardIdx, Long fileIdx) {
        return fileRepository
                .findByBoardEntity_IdxAndIdxAndState(boardIdx, fileIdx, CommonState.ENABLE)
                .orElseThrow(() -> new NoSuchElementException("파일을 찾을 수 없음"));
    }

    @Override
    public List<FileEntity> selectFileList(Long boardIdx) {
        return boardRepository
                .findById(boardIdx)
                .orElseThrow(() -> new RuntimeException("못찾음")).getFileList();
    }
}
