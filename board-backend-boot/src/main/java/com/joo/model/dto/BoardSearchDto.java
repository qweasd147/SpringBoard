package com.joo.model.dto;

import com.joo.common.state.EnumCodeType;
import com.joo.model.BaseModel;
import com.joo.model.dto.validator.AvailableCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@NoArgsConstructor
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

        SUBJECT(0, "제목"), CONTENTS(1, "내용");//,TAG("태그");

        private int code;
        private String description;

        SearchKeyWord(int code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public int getCode() {
            return code;
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
                    .anyMatch(searchKeyWord -> searchKeyWord.name().equals(upperCondition));
        }
    }

    @Builder
    public BoardSearchDto(int boardIdx, String searchCondition, String searchKeyWord) {
        this.boardIdx = boardIdx;
        this.searchCondition = searchCondition;
        this.searchKeyWord = searchKeyWord;
    }
}
