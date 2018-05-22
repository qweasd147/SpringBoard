package com.joo.api.board.vo;

import java.util.Date;
import java.util.List;

public class BoardVo {

    private int idx;
    private String subject;
    private String contents;
    private int hits;
    private int state;
    private String writer;
    private Date regDate;
    private List<FileVo> fileList;

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
                '}';
    }
}
