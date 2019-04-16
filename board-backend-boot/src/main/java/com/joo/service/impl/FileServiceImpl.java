package com.joo.service.impl;

import com.joo.common.state.CommonState;
import com.joo.model.dto.FileDto;
import com.joo.model.entity.FileEntity;
import com.joo.repository.BoardRepository;
import com.joo.repository.FileRepository;
import com.joo.service.BaseService;
import com.joo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl extends BaseService implements FileService{

    @Autowired
    private FileRepository fileRepository;

    @Autowired
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
