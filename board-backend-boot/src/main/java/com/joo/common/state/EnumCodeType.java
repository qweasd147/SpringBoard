package com.joo.common.state;

public interface EnumCodeType {

    /**
     * 공통 코드로 사용할 코드값을 반환한다.
     * @return
     */
    int getCode();

    /**
     * 해당 코드의 설명을 반환한다.
     * @return
     */
    String getDescription();
}
