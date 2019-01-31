package com.joo.common.state;


import java.util.Arrays;
import java.util.NoSuchElementException;

public enum CommonState implements EnumCodeType{
    ENABLE(0, "사용가능"), DELETE(1, "삭제"), LOCKED(2, "잠금"), EXPIRED(3, "만료");

    private int state;
    private String description;

    CommonState(int state, String description) {
        this.state = state;
        this.description = description;
    }

    @Override
    public int getCode() {
        return this.state;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public int getState(){
        return  this.state;
    }

    public static CommonState findStateByCode(int findState) {
        return Arrays.stream(CommonState.values())
                .filter(userState -> userState.getState() == findState)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("알 수 없는 board state code. code : "+findState));
    }
}
