package com.joo.model.entity;

import com.joo.model.BaseModel;
import com.joo.model.dto.BoardSearchDto;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class BoardSearchEntity extends BaseModel implements Serializable{

    private Long boardIdx;
    private String searchCondition = "";
    private String searchKeyWord = "";

    @Builder
    public BoardSearchEntity(Long boardIdx, String searchCondition, String searchKeyWord) {
        this.boardIdx = boardIdx;
        this.searchCondition = searchCondition;
        this.searchKeyWord = searchKeyWord;
    }

    public BoardSearchDto toDto(){
        return convertType(this, BoardSearchDto.class);
    }
}
