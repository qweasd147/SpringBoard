package com.joo.api.board.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(indexName = "joo", type = "board")
public class BoardEsVo implements Serializable{

    private static final long serialVersionUID = 3588196752802751105L;
    
    @Id
    private int idx;
    private String subject;
    private String contents;
    private int hits;
    private int state;
    private String writer;
    private Date regDate;

    @Field( type = FieldType.Nested)
    private List<FileVo> fileList;
    @Field( type = FieldType.Nested)
    private List<?> tagList;

    public BoardEsVo() {}

    public BoardEsVo(int idx, String subject, String contents, int hits, int state, String writer, Date regDate, List<FileVo> fileList, List<?> tagList) {
        this.idx = idx;
        this.subject = subject;
        this.contents = contents;
        this.hits = hits;
        this.state = state;
        this.writer = writer;
        this.regDate = regDate;
        this.fileList = fileList;
        this.tagList = tagList;
    }

    public BoardEsVo(BoardVo vo){
        this.idx = vo.getIdx();
        this.subject = vo.getSubject();
        this.contents = vo.getContents();
        this.hits = vo.getHits();
        this.state = vo.getState();
        this.writer = vo.getWriter();
        this.regDate = vo.getRegDate();
        this.fileList = vo.getFileList();
        this.tagList = vo.getTagList();
    }

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
        return "BoardEsVo{" +
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
