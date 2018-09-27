package com.joo.api.board.vo;

import com.joo.api.board.vo.validator.AvailableCondition;
import com.joo.api.common.EnumCodeType;

import java.io.Serializable;
import java.util.Arrays;

public class BoardSearchVo implements Serializable{

    private static final long serialVersionUID = -8216665660181770998L;

    /**
     * 검색 조건 목록 enum
     */
    public enum SearchKeyWord implements EnumCodeType{

        SUBJECT("제목"), CONTENTS("내용");//,TAG("태그");

        private String description;

        SearchKeyWord(String description) {
            this.description = description;
        }

        @Override
        public String getCode() {
            return name();
        }

        @Override
        public String getDescription() {
            return description;
        }

        /**
         * 등록된 검색조건(기준)에 있는지 조회한다.
         * @param condition
         * @return
         */
        public static boolean contains(String condition){
            return Arrays.stream(SearchKeyWord.values())
                    .anyMatch(searchKeyWord -> searchKeyWord.getCode().equals(condition));
        }
    }
    
    private int pageIdx = 1;
    private int boardIdx;

    @AvailableCondition
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
