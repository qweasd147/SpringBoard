package com.joo.model.dto;

import com.joo.common.state.CommonState;
import com.joo.model.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class FileDto extends BaseDto<String> implements Serializable{

    private static final long serialVersionUID = 2956026631315470683L;

    private Long idx;
    private String contentType;
    private String filePath;
    private String originFileName;
    private String saveFileName;
    private long fileSize;
    private CommonState state;
    private BoardDto boardDto;

    @Builder(toBuilder = true)
    public FileDto(Long idx, String contentType, String filePath, String originFileName, String saveFileName, long fileSize, CommonState state, BoardDto boardDto) {
        this.idx = idx;
        this.contentType = contentType;
        this.filePath = filePath;
        this.originFileName = originFileName;
        this.saveFileName = saveFileName;
        this.fileSize = fileSize;
        this.state = state;
        this.boardDto = boardDto;
    }

    public static  FileDto of(FileEntity fileDto){
        return FileDto.builder()
            .contentType(fileDto.getContentType())
            .filePath(fileDto.getFilePath())
            .originFileName(fileDto.getOriginFileName())
            .saveFileName(fileDto.getSaveFileName())
            .fileSize(fileDto.getFileSize())
            .state(fileDto.getState())
            .build();
    }

    @Override
    public FileEntity toEntity(){
        return FileEntity.builder()
            .contentType(this.contentType)
            .filePath(this.filePath)
            .originFileName(this.originFileName)
            .saveFileName(this.saveFileName)
            .fileSize(this.fileSize)
            .state(this.state)
            .boardEntity(this.boardDto.toEntity())
            .build();
    }
}
