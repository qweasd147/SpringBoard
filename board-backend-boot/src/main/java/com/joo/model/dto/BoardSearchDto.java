package com.joo.model.dto;

import com.joo.model.BaseModel;
import com.joo.model.entity.BoardSearchEntity;

import java.io.Serializable;

public class BoardSearchDto extends BaseModel implements Serializable{

    private static final long serialVersionUID = -8216665660181770998L;

    private int boardIdx;

    //@AvailableCondition
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

    public BoardSearchEntity toEntity(){
        return convertType(this, BoardSearchEntity.class);
    }
}
