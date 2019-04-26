package com.joo.model.dto;

import com.joo.common.state.CommonState;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class BoardUpdateRequestDto implements Serializable {

    private static final long serialVersionUID = 8343225276477963390L;

    @NotBlank(message = "제목 입력")
    private String subject;
    @NotBlank(message = "내용 입력")
    private String contents;
    private List<?> tagList = new ArrayList<>();
    private List<MultipartFile> uploadFiles = new ArrayList<>();
    private List<Long> deleteFiles = new ArrayList<>();
    private CommonState state = CommonState.ENABLE;

    @Builder
    public BoardUpdateRequestDto(String subject, String contents, List<?> tagList, List<MultipartFile> uploadFiles, List<Long> deleteFiles) {
        this.subject = subject;
        this.contents = contents;
        this.tagList = tagList;
        this.uploadFiles = uploadFiles;
        this.deleteFiles = deleteFiles;
    }

    public BoardEntity toEntity(){
        return BoardEntity.builder()
            .subject(this.subject)
            .contents(this.contents)
            .state(this.state)
            .build();
    }
}
