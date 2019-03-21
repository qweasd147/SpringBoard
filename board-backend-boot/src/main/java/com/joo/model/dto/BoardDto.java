package com.joo.model.dto;


import com.joo.common.state.CommonState;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto extends BaseDto<String> implements Serializable {

    private static final long serialVersionUID = 6348958289640869735L;

    private Long idx;
    @NotBlank(message = "제목 입력")
    private String subject;
    @NotBlank(message = "내용 입력")
    private String contents;
    private int hits;
    private CommonState state;
    private List<FileDto> fileList;
    private List<?> tagList;

    @Builder(toBuilder = true)
    public BoardDto(Long idx, String subject, String contents, int hits, CommonState state, List<FileDto> fileList, List<?> tagList) {
        this.idx = idx;
        this.subject = subject;
        this.contents = contents;
        this.hits = hits;
        this.state = state;
        this.fileList = fileList;
        this.tagList = tagList;
    }

    @Override
    public BoardEntity toEntity(){
        return convertType(this, BoardEntity.class);
    }

    /**
     * 순환 참조를 포함하여 dto화 시킨다.
     * @return
     */
    public BoardEntity toEntityWithCircular() {
        BoardEntity boardEntity = this.toEntity();

        List<FileEntity> fileEntityList = boardEntity.getFileList();
        if(fileEntityList != null)
            fileEntityList.forEach(fileEntity -> fileEntity.setBoardEntity(boardEntity));
        return boardEntity;
    }
}
