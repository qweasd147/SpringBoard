package com.joo.model.dto;

import com.joo.common.state.CommonState;
import com.joo.model.entity.BoardEntity;
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
public class BoardWriteRequestDto implements Serializable {

    private static final long serialVersionUID = 6785492672586208332L;

    @NotBlank(message = "제목 입력")
    private String subject;
    @NotBlank(message = "내용 입력")
    private String contents;
    private List<?> tagList;
    private List<MultipartFile> uploadFiles = new ArrayList<>();


    @Builder
    public BoardWriteRequestDto(String subject, String contents, List<?> tagList, List<MultipartFile> uploadFiles) {
        this.subject = subject;
        this.contents = contents;
        this.tagList = tagList;
        this.uploadFiles = uploadFiles;
    }

    public BoardEntity toEntity(){
        return BoardEntity.builder()
            .subject(this.subject)
            .contents(this.contents)
            .state(CommonState.ENABLE)
            .build();
    }
}
