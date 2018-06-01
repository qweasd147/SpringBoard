package com.joo.api.board.vo;

import java.io.Serializable;

public class BoardSearchVo implements Serializable{

    private static final long serialVersionUID = -8216665660181770998L;

    public enum searchKeyWord{
        subject, contents, tag
    }
    
    private int pageIdx = 1;
    private int boardIdx;
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

    @Override
    public String toString() {
        return "BoardSearchVo{" +
                "pageIdx=" + pageIdx +
                ", searchCondition='" + searchCondition + '\'' +
                ", searchKeyWord='" + searchKeyWord + '\'' +
                ", rowsPerPage=" + rowsPerPage +
                '}';
    }
}
