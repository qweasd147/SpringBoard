package com.joo.api.board.service;

import com.joo.api.board.vo.FileVo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<FileVo> uploadFiles(MultipartFile[] files);

    //TODO : 차후 board랑 file 연관관계 끊는게 좋을꺼 같음
    FileVo selectFileVo(int boardIdx, int fileIdx);

    List<FileVo> insertFileList(List<FileVo> fileVoList);

    void insertFileMapping(int boardIdx, List<FileVo> fileVoList);

    void deleteFileMappingByFileID(List<Integer> fileIdxList);

    Resource getFileResouce(FileVo fileVo);
}
