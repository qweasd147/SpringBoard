package com.joo.model.dto;

import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class FileDto extends BaseDto implements Serializable{

    private static final long serialVersionUID = 2956026631315470683L;

    private Long idx;
    private String contentType;
    private String filePath;
    private String originFileName;
    private String saveFileName;
    private long fileSize;
    private int state;
    private BoardDto boardDto;

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }

    public void setBoardDto(BoardDto boardDto) {
        this.boardDto = boardDto;
    }

    @Override
    public FileEntity toEntity(){
        return convertType(this, FileEntity.class);
    }
}
