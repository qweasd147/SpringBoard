package com.joo.api.board.mapper;

import com.joo.api.board.vo.FileVo;
import com.joo.api.common.EnumCodeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository(value = "fileMapper")
public interface FileMapper {

    enum XmlMappingID implements EnumCodeType {

        boardIdx("게시판 고유번호"), fileIdx("파일 고유번호");

        private String description;

        XmlMappingID(String description){
            this.description = description;
        }

        @Override
        public String getCode() {
            return name();
        }

        @Override
        public String getDescription(){
            return description;
        }
    }

    /**
     * 파일 정보를 조회한다.
     * @param fileInfo
     * @return
     */
    FileVo selectFile(Map<String, Integer> fileInfo);

    /**
     * 파일 목록 정보를 조회한다.
     * @param boardIdx
     * @return
     */
    List<FileVo> selectFileList(int boardIdx);

    /**
     * 게시글에 등록된 파일들의 기본적인 정보만 조회한다.
     * @param boardIdx
     * @return
     */
    List<FileVo> selectBasicFileList(int boardIdx);

    /**
     * 파일을 등록한다.
     * @param fileVo
     */
    void insertFile(FileVo fileVo);

    /**
     * 파일과 게시글 맵핑정보를 등록한다.
     * @param fileInfo
     */
    void insertMapping(Map<String, Integer> fileInfo);

    /**
     * 파일을 삭제 처리한다.
     * @param fileInfo
     */
    void deleteFile(Map<String, Integer> fileInfo);

    /**
     * 하나의 게시글에 등록된 파일들을 삭제 처리한다.
     * @param boardIdx
     */
    void deleteFileMappingByBoardID(int boardIdx);

    /**
     * 특정 파일 하나를 삭제 처리한다.
     * @param fileIdx
     */
    void deleteFileMappingByFileID(int fileIdx);
}
