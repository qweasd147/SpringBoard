package com.joo.service.impl;

import com.joo.model.dto.BoardDto;
import com.joo.model.dto.FileDto;
import com.joo.model.entity.FileEntity;
import com.joo.model.state.BoardState;
import com.joo.repository.BoardRepository;
import com.joo.repository.FileRepository;
import com.joo.service.BaseService;
import com.joo.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl extends BaseService implements FileService{

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public FileDto selectFileOne(Long boardIdx, Long fileIdx) {
        return fileRepository
                .findByBoardEntity_IdxAndIdxAndState(boardIdx, fileIdx, BoardState.ENABLE.getState())
                .orElseThrow(() -> new NoSuchElementException("파일을 찾을 수 없음")).toDto();
    }

    @Override
    public List<FileDto> selectFileList(Long boardIdx) {

        List<FileEntity> fileEntityList =
                boardRepository
                    .findById(boardIdx)
                    .orElseThrow(() -> new RuntimeException("못찾음")).getFileList();
        return fileEntityList.stream().map(FileEntity::toDto).collect(Collectors.toList());
    }
}
