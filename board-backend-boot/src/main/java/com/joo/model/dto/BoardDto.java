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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoardDto extends BaseDto<String> implements Serializable {

    private static final long serialVersionUID = 6348958289640869735L;

    @Setter
    private Long idx;
    @NotBlank(message = "제목 입력")
    private String subject;
    @NotBlank(message = "내용 입력")
    private String contents;
    private int hits;
    private CommonState state;
    private List<FileDto> fileList = new ArrayList<>();
    private List<?> tagList;

    @Builder(toBuilder = true)
    private BoardDto(Long idx, String subject, String contents, int hits, CommonState state, List<FileDto> fileList, List<?> tagList) {
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

        List<FileEntity> fileEntities = fileList.stream()
                .map(FileDto::toEntity)
                .collect(Collectors.toList());

        return BoardEntity.builder()
            .subject(this.subject)
            .contents(this.contents)
            .hits(this.hits)
            .state(this.state)
            .fileList(fileEntities)
            .tagList(this.tagList)
            .build();
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

    public List<FileDto> addFile(FileDto fileDto){
        fileList.add(fileDto);

        return fileList;
    }

    public List<FileDto> addFiles(List<FileDto> fileDtos){
        fileList.addAll(fileDtos);

        return fileList;
    }
}
