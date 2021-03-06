package com.joo.model.dto.limited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joo.model.dto.FileDto;
import com.joo.model.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자에게 필요한 데이터만 보내줄 목적으로 만들어진 dto
 */
@Getter
public class LimitedFileDto {

    private Long idx;
    private String contentType;
    private String originFileName;
    private long fileSize;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedDate;

    @Builder
    public LimitedFileDto(Long idx, String contentType, String originFileName, long fileSize, String createdBy, LocalDateTime createdDate, String lastModifiedBy, LocalDateTime lastModifiedDate) {
        this.idx = idx;
        this.contentType = contentType;
        this.originFileName = originFileName;
        this.fileSize = fileSize;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static LimitedFileDto of(FileEntity fileEntity){
        return LimitedFileDto.builder()
            .idx(fileEntity.getIdx())
            .contentType(fileEntity.getContentType())
            .originFileName(fileEntity.getOriginFileName())
            .fileSize(fileEntity.getFileSize())
            .createdBy(fileEntity.getCreatedBy())
            .createdDate(fileEntity.getCreatedDate())
            .lastModifiedBy(fileEntity.getLastModifiedBy())
            .lastModifiedDate(fileEntity.getLastModifiedDate())
            .build();
    }
}
