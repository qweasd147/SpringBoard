package com.joo.api.board.service;

import com.joo.api.board.vo.FileVo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * 파일 목록을 서버에 저장한다.
     * DB에 저장x
     * @param files
     * @return
     */
    List<FileVo> uploadFiles(MultipartFile[] files);

    /**
     * 단건 파일 하나를 조회한다.
     * @param boardIdx
     * @param fileIdx
     * @return
     */
    FileVo selectFileVo(int boardIdx, int fileIdx);     //TODO : 차후 board랑 file 연관관계 끊는게 좋을꺼 같음

    /**
     * 게시글에 등록된 파일 목록을 조회한다.
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
     * 파일들 정보를 DB에 저장한다.
     * @param fileVoList
     * @return
     */
    List<FileVo> insertFileList(List<FileVo> fileVoList);

    /**
     * 파일 맵핑 정보를 저장한다.
     * @param boardIdx
     * @param fileVoList
     */
    void insertFileMapping(int boardIdx, List<FileVo> fileVoList);

    /**
     * 요청한 파일 목록의 정보를 삭제한다.
     * @param fileIdxList
     */
    void deleteFileMappingByFileID(List<Integer> fileIdxList);

    /**
     * 하나의 게시글에 등록된 파일들을 삭제 처리한다.
     * @param boardIdx
     */
    void deleteFileMappingByBoardID(int boardIdx);

    /**
     * 해당 파일의 리소스 정보를 얻는다.
     * @param fileVo
     * @return
     */
    Resource getFileResouce(FileVo fileVo);
}
