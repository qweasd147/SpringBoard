package com.joo.service;

import com.joo.model.dto.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * 단건 파일 하나를 조회한다.
     * @param boardIdx
     * @param fileIdx
     * @return
     */
    FileDto selectFileOne(Long boardIdx, Long fileIdx);     //TODO : 차후 board랑 file 연관관계 끊는게 좋을꺼 같음

    /**
     * 게시글에 등록된 파일 목록을 조회한다.
     * @param boardIdx
     * @return
     */
    List<FileDto> selectFileList(Long boardIdx);
}
