package com.joo.model.entity;

import com.joo.model.BaseModel;
import com.joo.model.dto.BoardSearchDto;

import java.io.Serializable;

public class BoardSearchEntity extends BaseModel implements Serializable{

    private int boardIdx;

    private String searchCondition = "";
    private String searchKeyWord = "";

    public int getBoardIdx() {
        return boardIdx;
    }

    public void setBoardIdx(int boardIdx) {
        this.boardIdx = boardIdx;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    public String getSearchKeyWord() {
        return searchKeyWord;
    }

    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }

    public BoardSearchDto toDto(){
        return convertType(this, BoardSearchDto.class);
    }
}
