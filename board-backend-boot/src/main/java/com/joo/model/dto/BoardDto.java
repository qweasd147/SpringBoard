package com.joo.model.dto;


import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.model.state.BoardState;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

public class BoardDto extends BaseDto<String> implements Serializable {

    private static final long serialVersionUID = 6348958289640869735L;

    private Long idx;
    @NotBlank(message = "제목 입력")
    private String subject;
    @NotBlank(message = "내용 입력")
    private String contents;
    private int hits;
    private BoardState boardState;
    private List<FileDto> fileList;
    private List<?> tagList;

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public BoardState getState() {
        return boardState;
    }

    public void setState(BoardState boardState) {
        this.boardState = boardState;
    }

    public void setState(int state) {
        this.boardState = BoardState.getStateByCode(state);
    }

    public List<FileDto> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileDto> fileList) {
        this.fileList = fileList;
    }

    public List<?> getTagList() {
        return tagList;
    }

    public void setTagList(List<?> tagList) {
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
