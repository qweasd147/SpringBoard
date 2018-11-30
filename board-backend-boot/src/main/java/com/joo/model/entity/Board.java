package com.joo.model.entity;

import com.joo.model.BaseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Board extends BaseEntity<String>{

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
    private LocalDateTime createDate;
    private List<File> fileList;

    private List<?> tagList;
}
