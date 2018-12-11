package com.joo.model.entity;

import com.joo.model.dto.BoardDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class BoardEntity extends BaseEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(nullable = false)
    @NotNull
    private String subject;

    @Column(nullable = false)
    @NotNull
    private String contents;
    private int hits;
    private int state;
    private String writer;

    //board 조회 시, file도 같이 조회, 삭제 시 연관 파일 삭제
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<FileEntity> fileList;

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

    public List<FileEntity> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileEntity> fileList) {
        this.fileList = fileList;
    }

    public List<?> getTagList() {
        return tagList;
    }

    public void setTagList(List<?> tagList) {
        this.tagList = tagList;
    }

    public BoardDto toDto(){
        return convertType(this, BoardDto.class);
    }
}
