package com.joo.model.dto.limited;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자에게 필요한 데이터만 보내줄 목적으로 만들어진 dto
 */
@Getter
public class LimitedBoardDto {

    private Long idx;
    private String subject;
    private String contents;
    private int hits;
    private List<LimitedFIleDto> fileList;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedDate;

    @Builder
    public LimitedBoardDto(Long idx, String subject, String contents, int hits, List<LimitedFIleDto> fileList, String createdBy, LocalDateTime createdDate, String lastModifiedBy, LocalDateTime lastModifiedDate) {
        this.idx = idx;
        this.subject = subject;
        this.contents = contents;
        this.hits = hits;
        this.fileList = fileList;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }
}
