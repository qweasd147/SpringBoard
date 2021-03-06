package com.joo.model.entity;


import com.joo.common.converter.CommonStateConverterImpl;
import com.joo.common.state.CommonState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class FileEntity extends BaseEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String contentType;
    private String filePath;
    private String originFileName;
    private String saveFileName;
    private long fileSize;
    @Convert(converter = CommonStateConverterImpl.class)
    private CommonState state;

    @ManyToOne
    @JoinColumn(name="board_idx", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BoardEntity boardEntity;

    @Builder(toBuilder = true)
    public FileEntity(String contentType, String filePath, String originFileName, String saveFileName, long fileSize, CommonState state, BoardEntity boardEntity) {
        this.contentType = contentType;
        this.filePath = filePath;
        this.originFileName = originFileName;
        this.saveFileName = saveFileName;
        this.fileSize = fileSize;
        this.state = state;
        this.boardEntity = boardEntity;
    }

    public void delete(){
        this.state = CommonState.DELETE;
    }
}
