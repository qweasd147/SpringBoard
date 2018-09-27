package com.joo.api.board.service;

import com.joo.api.board.mapper.FileMapper;
import com.joo.api.board.vo.FileVo;
import com.joo.exception.BusinessException;
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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileServiceImpl implements FileService{

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileMapper fileMapper;

    private final Path baseUploadPath;

    public FileServiceImpl(@Value("#{appProperty['file.upload.dir']}")String baseUploadPathStr) {
        baseUploadPath = Paths.get(baseUploadPathStr);
    }

    @Override
    public List<FileVo> uploadFiles(MultipartFile[] files) {

        List<FileVo> uploadedList = new ArrayList<>();

        for (int i=0;i< files.length;i++){
            MultipartFile file = files[i];
            if(!isAvaliableFile(file))  continue;       //check file validate

            String saveFileName = UUID.randomUUID().toString();
            String originFileName = file.getOriginalFilename();

            try(InputStream fileInputStream = file.getInputStream()){
                //중복된 파일명이 있을 시, 덮어씌움. uuid로 저장해서 사실상 그럴일 없다고 가정해도 됨
                Files.copy(fileInputStream, baseUploadPath.resolve(saveFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileVo fileVo = new FileVo();

            fileVo.setContentType(file.getContentType());
            fileVo.setFilePath(baseUploadPath.toAbsolutePath().toString());
            fileVo.setOriginFileName(originFileName);
            fileVo.setSaveFileName(saveFileName);
            fileVo.setFileSize(file.getSize());
            fileVo.setRegDate(new Date());

            uploadedList.add(fileVo);
        }

        return uploadedList;
    }

    @Override
    public FileVo selectFileVo(int boardIdx, int fileIdx) {

        Map<String, Integer> params = new HashMap<>();

        params.put(FileMapper.XmlMappingID.boardIdx.getCode(), boardIdx);
        params.put(FileMapper.XmlMappingID.fileIdx.getCode(), fileIdx);

        return fileMapper.selectFile(params);
    }

    @Override
    public List<FileVo> selectFileList(int boardIdx) {
        return fileMapper.selectFileList(boardIdx);
    }

    @Override
    public List<FileVo> selectBasicFileList(int boardIdx) {
        return fileMapper.selectBasicFileList(boardIdx);
    }

    @Override
    public List<FileVo> insertFileList(List<FileVo> fileVoList) {

        for(int i=0;i<fileVoList.size();i++){
            fileMapper.insertFile(fileVoList.get(i));
        }
        return fileVoList;
    }

    @Override
    public void insertFileMapping(int boardIdx, List<FileVo> fileVoList) {
        if(fileVoList == null || fileVoList.size() == 0)    return;

        Map<String, Integer> params = new HashMap<>();
        params.put(FileMapper.XmlMappingID.boardIdx.getCode(), boardIdx);

        for(int i=0;i<fileVoList.size();i++){
            params.put(FileMapper.XmlMappingID.fileIdx.getCode(), fileVoList.get(i).getIdx());
            fileMapper.insertMapping(params);
        }
    }

    @Override
    public void deleteFileMappingByFileID(List<Integer> fileIdxList) {

        if(fileIdxList == null || fileIdxList.size() == 0)  return;

        for(int i=0;i<fileIdxList.size();i++){
            fileMapper.deleteFileMappingByFileID(fileIdxList.get(i));
        }
    }

    @Override
    public void deleteFileMappingByBoardID(int boardIdx) {
        fileMapper.deleteFileMappingByBoardID(boardIdx);
    }

    @Override
    public Resource getFileResouce(FileVo fileVo) {

        Path file = Paths.get(fileVo.getFilePath(), fileVo.getSaveFileName());

        Resource resource;
        try {
            resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return resource;
            }else{
                throw new MalformedURLException("파일을 읽을 수 없음");
            }
        } catch (MalformedURLException e) {
            String fileFullPath = fileVo.getFilePath()+"/"+fileVo.getSaveFileName()+"("+fileVo.getOriginFileName()+")";
            logger.error("file ERROR!");
            logger.error(fileFullPath);
            logger.error(e.getMessage());

            throw new BusinessException("HTTP_500","파일 처리 오류");
        }
    }

    private boolean isAvaliableFile(MultipartFile file){

        if(file.isEmpty()){
            logger.warn("비어있는 파일");
            return false;
        }

        String fileName = file.getOriginalFilename();
        if(fileName.contains("..")){
            logger.warn("상대경로 정보가 들어가 있는 파일명은 업로드 할 수 없음"+file.getOriginalFilename());
            return false;
        }

        return true;
    }
}
