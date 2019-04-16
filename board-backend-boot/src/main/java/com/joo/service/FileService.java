package com.joo.service;

import com.joo.model.entity.FileEntity;

import java.util.List;

public interface FileService {

    /**
     * 단건 파일 하나를 조회한다.
     * @param boardIdx
     * @param fileIdx
     * @return
     */
    FileEntity selectFileOne(Long boardIdx, Long fileIdx);     //TODO : 차후 board랑 file 연관관계 끊는게 좋을꺼 같음

    /**
     * 게시글에 등록된 파일 목록을 조회한다.
     * @param boardIdx
     * @return
     */
    List<FileEntity> selectFileList(Long boardIdx);
}
