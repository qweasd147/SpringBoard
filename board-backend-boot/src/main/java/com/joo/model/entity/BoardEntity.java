package com.joo.model.entity;

import com.joo.common.converter.CommonStateImpl;
import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardDto;
import com.joo.model.dto.FileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class BoardEntity extends BaseEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    @NotNull
    private String subject;

    @Column(nullable = false)
    @NotNull
    @Lob
    private String contents;
    private int hits;
    @Convert(converter = CommonStateImpl.class)
    private CommonState state;

    //board 조회 시 file도 같이 조회, 삭제 시 연관 파일 삭제, boardEntity 변수로 맵핑
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "boardEntity")
    private List<FileEntity> fileList;

    @Transient
    private List<?> tagList;

    @Builder(toBuilder = true)
    public BoardEntity(String subject, String contents, int hits, CommonState state, List<FileEntity> fileList, List<?> tagList) {
        this.subject = subject;
        this.contents = contents;
        this.hits = hits;
        this.state = state;
        this.fileList = fileList;
        this.tagList = tagList;
    }

    public List<FileEntity> addFile(FileEntity fileEntity){
        fileList.add(fileEntity);

        return fileList;
    }

    public List<FileEntity> addFiles(List<FileEntity> fileEntities){
        fileList.addAll(fileEntities);

        return fileList;
    }

    public void delete(){
        this.state = CommonState.DELETE;
    }
}
