package com.joo.model.dto;

import com.joo.common.EnumCodeType;
import com.joo.model.BaseModel;
import com.joo.model.dto.validator.AvailableCondition;
import com.joo.model.entity.BoardSearchEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

public class BoardSearchDto extends BaseModel implements Serializable{

    private static final long serialVersionUID = -8216665660181770998L;

    private int boardIdx;

    //@NotBlank(message = "검색조건 입력")
    @AvailableCondition
    private String searchCondition = "";
    private String searchKeyWord = "";

    /**
     * 허용할 검색 조건 목록 enum
     */
    public enum SearchKeyWord implements EnumCodeType {

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
            String upperCondition = condition.toUpperCase();
            return Arrays.stream(SearchKeyWord.values())
                    .anyMatch(searchKeyWord -> searchKeyWord.getCode().equals(upperCondition));
        }
    }


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
}
