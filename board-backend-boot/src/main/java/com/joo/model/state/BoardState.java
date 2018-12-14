package com.joo.model.state;

import com.joo.common.EnumCodeType;
import com.joo.exception.BusinessException;

import java.util.Arrays;

public enum BoardState implements EnumCodeType {
    ENABLE(0), DELETE(1), LOCKED(2);

    private int status;

    BoardState(int status) {
        this.status = status;
    }

    @Override
    public String getCode() {
        return String.valueOf(this.status);
    }

    @Override
    public String getDescription() {
        return this.name();
    }

    public int getState(){
        return  this.status;
    }

    public static BoardState getStateByCode(int boardStatusCode) {
        return Arrays.stream(BoardState.values())
                .filter(userState -> userState.getState() == boardStatusCode)
                .findFirst()
                .orElseThrow(() -> new BusinessException(null,"알 수 없는 board state code. code : "+boardStatusCode));
    }
}
