package com.joo.service.impl;

import com.joo.model.dto.FileDto;
import com.joo.model.entity.FileEntity;
import com.joo.repository.FileRepository;
import com.joo.service.BaseService;
import com.joo.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class FileServiceImpl extends BaseService implements FileService{

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileRepository fileRepository;

    private final Path baseUploadPath;

    public FileServiceImpl(Path baseUploadPath) {
        this.baseUploadPath = baseUploadPath;
    }

    @Override
    public List<FileDto> uploadFilesInPhysical(MultipartFile[] multipartFiles) {
        List<FileDto> uploadedList = new ArrayList<>();

        if(Objects.isNull(multipartFiles))   return Collections.EMPTY_LIST;

        for (int i=0;i< multipartFiles.length;i++){
            MultipartFile file = multipartFiles[i];
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

            FileDto fileDto = new FileDto();

            fileDto.setContentType(file.getContentType());
            fileDto.setFilePath(baseUploadPath.toAbsolutePath().toString());
            fileDto.setOriginFileName(originFileName);
            fileDto.setSaveFileName(saveFileName);
            fileDto.setFileSize(file.getSize());

            uploadedList.add(fileDto);
        }

        return uploadedList;
    }

    @Override
    public FileDto selectFile(int boardIdx, int fileIdx) {
        //TODO : check board idx
        return fileRepository.findById((long) fileIdx).orElseThrow(()->new RuntimeException("못찾음")).toDto();
    }

    @Override
    public List<FileDto> selectFileList(int boardIdx) {

        return null;
    }

    @Override
    public List<FileDto> selectBasicFileList(int boardIdx) {
        return null;
    }

    @Override
    public List<FileDto> insertFileList(List<FileDto> fileDtoList) {
        return null;
    }

    @Override
    public void insertFileMapping(int boardIdx, List<FileDto> fileDtoList) {

    }

    @Override
    public void deleteFileMappingByFileID(List<Integer> fileIdxList) {

    }

    @Override
    public void deleteFileMappingByBoardID(int boardIdx) {
        //fileRepository.deleteById();
    }

    @Override
    public Resource getFileResouce(FileDto fileDto) {
        Path file = Paths.get(fileDto.getFilePath(), fileDto.getSaveFileName());

        Resource resource;
        try {
            resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return resource;
            }else{
                throw new MalformedURLException("파일을 읽을 수 없음");
            }
        } catch (MalformedURLException e) {
            String fileFullPath = fileDto.getFilePath()+"/"+fileDto.getSaveFileName()+"("+fileDto.getOriginFileName()+")";
            logger.error("file ERROR!");
            logger.error(fileFullPath);
            logger.error(e.getMessage());

            //throw new BusinessException("HTTP_500","파일 처리 오류");
            return null;
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
