package com.joo.service;

import com.joo.model.entity.File;
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
    List<File> uploadFilesInPhysical(MultipartFile[] files);

    /**
     * 단건 파일 하나를 조회한다.
     * @param boardIdx
     * @param fileIdx
     * @return
     */
    File selectFile(int boardIdx, int fileIdx);     //TODO : 차후 board랑 file 연관관계 끊는게 좋을꺼 같음

    /**
     * 게시글에 등록된 파일 목록을 조회한다.
     * @param boardIdx
     * @return
     */
    List<File> selectFileList(int boardIdx);

    /**
     * 게시글에 등록된 파일들의 기본적인 정보만 조회한다.
     * @param boardIdx
     * @return
     */
    List<File> selectBasicFileList(int boardIdx);

    /**
     * 파일들 정보를 DB에 저장한다.
     * @param FileList
     * @return
     */
    List<File> insertFileList(List<File> FileList);

    /**
     * 파일 맵핑 정보를 저장한다.
     * @param boardIdx
     * @param FileList
     */
    void insertFileMapping(int boardIdx, List<File> FileList);

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
     * @param File
     * @return
     */
    Resource getFileResouce(File File);
}
