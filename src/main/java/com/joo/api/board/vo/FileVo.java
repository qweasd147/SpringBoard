package com.joo.api.board.vo;

import java.util.Date;

public class FileVo {

    private int idx;
    private String contentType;
    private String filePath;
    private String originFileName;
    private String saveFileName;
    private int fileSize;
    private String writer;
    private Date regDate;
    private int state;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
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

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "FileVo{" +
                "idx=" + idx +
                ", contentType='" + contentType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", originFileName='" + originFileName + '\'' +
                ", saveFileName='" + saveFileName + '\'' +
                ", fileSize=" + fileSize +
                ", writer='" + writer + '\'' +
                ", regDate=" + regDate +
                ", state=" + state +
                '}';
    }
}
