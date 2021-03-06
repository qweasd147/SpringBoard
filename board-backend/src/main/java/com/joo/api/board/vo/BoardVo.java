package com.joo.api.board.vo;


import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BoardVo implements Serializable {

    private static final long serialVersionUID = 6348958289640869735L;

    private int idx;

    @NotEmpty
    private String subject;
    @NotEmpty
    private String contents;
    private int hits;
    private int state;
    private String writer;
    private Date regDate;
    private List<FileVo> fileList;
    private List<?> tagList;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public List<FileVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileVo> fileList) {
        this.fileList = fileList;
    }

    public List<?> getTagList() {
        return tagList;
    }

    public void setTagList(List<?> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return "BoardVo{" +
                "idx=" + idx +
                ", subject='" + subject + '\'' +
                ", contents='" + contents + '\'' +
                ", hits=" + hits +
                ", state=" + state +
                ", writer='" + writer + '\'' +
                ", regDate=" + regDate +
                ", fileList=" + fileList +
                ", tagList=" + tagList +
                '}';
    }
}
