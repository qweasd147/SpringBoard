package com.joo.model.entity;

import com.joo.common.converter.CommonStateImpl;
import com.joo.common.state.CommonState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private CommonState state = CommonState.ENABLE;

    //board 조회 시 file도 같이 조회, 삭제 시 연관 파일 삭제, boardEntity 변수로 맵핑
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "boardEntity")
    private List<FileEntity> fileList = new ArrayList<>();

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
        fileList.add(this.initFileEntityWithThis(fileEntity));

        return fileList;
    }

    public List<FileEntity> addFiles(List<FileEntity> fileEntities){

        List<FileEntity> initFileEntities = fileEntities.stream()
                .map(fileEntity -> this.initFileEntityWithThis(fileEntity))
                .collect(Collectors.toList());

        fileList.addAll(initFileEntities);

        return fileList;
    }

    public void delete(){
        this.state = CommonState.DELETE;
    }

    private FileEntity initFileEntityWithThis(FileEntity fileEntity){
        return fileEntity.toBuilder()
            .boardEntity(this)
            .build();
    }

    public void deleteFiles(List<Long> fileIdxs){
        List<FileEntity> newFiles = this.fileList.stream()
                .filter((fileEntity) -> !Objects.isNull(fileEntity.getIdx()))//새로 추가 예정인 파일은 필터링
                .filter(fileEntity -> fileIdxs.contains(fileEntity.getIdx()))
                .collect(Collectors.toList());

        this.fileList = newFiles;
    }
}
