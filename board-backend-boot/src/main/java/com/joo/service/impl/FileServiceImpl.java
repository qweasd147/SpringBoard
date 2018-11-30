package com.joo.service.impl;

import com.joo.model.entity.File;
import com.joo.repository.FileRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileRepository fileRepository;

    private final Path baseUploadPath;

    public FileServiceImpl(Path baseUploadPath) {
        this.baseUploadPath = baseUploadPath;
    }

    @Override
    public List<File> uploadFilesInPhysical(MultipartFile[] files) {
        List<File> uploadedList = new ArrayList<>();

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

            File fileVo = new File();

            fileVo.setContentType(file.getContentType());
            fileVo.setFilePath(baseUploadPath.toAbsolutePath().toString());
            fileVo.setOriginFileName(originFileName);
            fileVo.setSaveFileName(saveFileName);
            fileVo.setFileSize(file.getSize());

            uploadedList.add(fileVo);
        }

        return uploadedList;
    }

    @Override
    public File selectFile(int boardIdx, int fileIdx) {
        return fileRepository.findById((long) fileIdx).orElseThrow(()->new RuntimeException("못찾음"));
    }

    @Override
    public List<File> selectFileList(int boardIdx) {

        return null;
    }

    @Override
    public List<File> selectBasicFileList(int boardIdx) {
        return null;
    }

    @Override
    public List<File> insertFileList(List<File> FileList) {
        return null;
    }

    @Override
    public void insertFileMapping(int boardIdx, List<File> FileList) {

    }

    @Override
    public void deleteFileMappingByFileID(List<Integer> fileIdxList) {

    }

    @Override
    public void deleteFileMappingByBoardID(int boardIdx) {
        //fileRepository.deleteById();
    }

    @Override
    public Resource getFileResouce(File fileEntity) {
        Path file = Paths.get(fileEntity.getFilePath(), fileEntity.getSaveFileName());

        Resource resource;
        try {
            resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return resource;
            }else{
                throw new MalformedURLException("파일을 읽을 수 없음");
            }
        } catch (MalformedURLException e) {
            String fileFullPath = fileEntity.getFilePath()+"/"+fileEntity.getSaveFileName()+"("+fileEntity.getOriginFileName()+")";
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
