package com.joo.utils;

import com.joo.exception.BusinessException;
import com.joo.model.dto.FileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

@Component
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private final Path baseUploadPath;

    public FileUtils(@Value("${file.upload.dir}")String fileUploadPath) {
        this.baseUploadPath = Paths.get(fileUploadPath);
    }

    /**
     * 파일 목록을 서버에 저장한다.
     * DB에 저장x
     * @param multipartFiles
     * @return
     */
    public List<FileDto> uploadFilesInPhysical(MultipartFile[] multipartFiles){

        List<FileDto> uploadedList = new ArrayList<>();

        if(Objects.isNull(multipartFiles))   return Collections.EMPTY_LIST;

        for (int i=0;i< multipartFiles.length;i++){
            MultipartFile file = multipartFiles[i];

            checkAvaliableFile(file);   //check file validate

            String saveFileName = UUID.randomUUID().toString();
            String originFileName = file.getOriginalFilename();

            try(InputStream fileInputStream = file.getInputStream()){
                //중복된 파일명이 있을 시, 덮어씌움. uuid로 저장해서 사실상 그럴일 없다고 가정해도 됨
                Files.copy(fileInputStream, baseUploadPath.resolve(saveFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (NoSuchFileException ne) {
                logger.error("파일 저장 에러. 파일을 찾을 수 없음");
                logger.error("save file name : " + saveFileName + ", origin file name : " + originFileName);
                logger.error("파일 경로 : " + baseUploadPath.toAbsolutePath().toString());
                logger.error(ne.getMessage());

                throw new RuntimeException("파일 올리기 오류");
            } catch (IOException ioe) {
                logger.error("파일 저장 에러");
                logger.error("save file name : " + saveFileName + ", origin file name : " + originFileName);
                logger.error(ioe.getMessage());

                throw new RuntimeException("파일 올리기 오류");
            }

            FileDto fileDto = FileDto.builder()
                .contentType(file.getContentType())
                .filePath(baseUploadPath.toAbsolutePath().toString())
                .originFileName(originFileName)
                .saveFileName(saveFileName)
                .fileSize(file.getSize())
                .build();

            uploadedList.add(fileDto);
        }

        return uploadedList;
    }

    public static void checkAvaliableFile(MultipartFile file){

        if(file.isEmpty()){
            logger.warn("비어있는 파일");
        }

        String fileName = file.getOriginalFilename();
        if(fileName.contains("..")){
            String warnMsg = "상대경로 정보가 들어가 있는 파일명은 업로드 할 수 없음" + file.getOriginalFilename();
            logger.warn(warnMsg);

            throw new BusinessException("99", warnMsg);
        }
    }

    /**
     * 해당 파일의 리소스 정보를 얻는다.
     * @param fileDto
     * @return
     */
    public static Resource getFileResouce(FileDto fileDto) {
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
        }
        return null;
    }
}
