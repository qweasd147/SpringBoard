package com.joo.api.board.mapper;

import com.joo.api.board.vo.FileVo;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository(value = "fileMapper")
public interface FileMapper {

    enum XmlMappingID {
        boardIdx, fileIdx
    }

    FileVo selectFile(Map<String, Integer> fileInfo);

    void insertFile(FileVo fileVo);

    void insertMapping(Map<String, Integer> fileInfo);

    void deleteFile(Map<String, Integer> fileInfo);

    void deleteFileMappingByBoardID(int boardIdx);

    void deleteFileMappingByFileID(int fileIdx);
}
