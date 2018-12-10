package com.joo.model.dto;

import com.joo.model.BaseModel;
import com.joo.model.entity.BoardSearchEntity;

import java.io.Serializable;

public class BoardSearchDto extends BaseModel implements Serializable{

    private static final long serialVersionUID = -8216665660181770998L;

    private int pageIdx = 1;
    private int boardIdx;

    //@AvailableCondition
    private String searchCondition = "";
    private String searchKeyWord = "";
    private int rowsPerPage = 10;       //페이지 당 출력할 row 갯수


    public int getBoardIdx() {
        return boardIdx;
    }

    public void setBoardIdx(int boardIdx) {
        this.boardIdx = boardIdx;
    }

    public int getPageIdx() {
        return pageIdx;
    }

    public void setPageIdx(int pageIdx) {
        this.pageIdx = pageIdx;
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

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public BoardSearchEntity toEntity(){
        return convertType(this, BoardSearchEntity.class);
    }
}
