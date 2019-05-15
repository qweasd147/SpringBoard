package com.joo.model.entity;

import com.joo.common.converter.CommonStateConverterImpl;
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

    @Convert(converter = CommonStateConverterImpl.class)
    private CommonState state = CommonState.ENABLE;

    //board 조회 시 file도 같이 조회, save 시 하위 객체로 insert, boardEntity 변수로 맵핑
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "boardEntity")
    private List<FileEntity> fileList = new ArrayList<>();

    @Transient
    private List<?> tagList;

    @Builder(toBuilder = true)
    public BoardEntity(String subject, String contents, int hits, CommonState state) {
        this.subject = subject;
        this.contents = contents;
        this.hits = hits;
        this.state = state;
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
            .state(CommonState.ENABLE)
            .build();
    }

    public void deleteFiles(List<Long> fileIdxs){
        this.fileList.stream()
            .filter((fileEntity) -> !Objects.isNull(fileEntity.getIdx()))//새로 추가 예정인 파일은 필터링
            .filter(fileEntity -> fileIdxs.contains(fileEntity.getIdx()))
            .forEach( fileEntity -> fileEntity.delete());
    }

    public void update(String subject, String contents){
        this.subject = subject;
        this.contents = contents;
    }
}
