package com.joo.common.converter;

import com.joo.common.state.EnumCodeType;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class EnumConvertUtils {

    private EnumConvertUtils() {}

    public static <T extends Enum<T> & EnumCodeType> T getEnumCodeTypeByCode(Class<T> enumClazz, int enumCode){

        //EnumSet.allOf(enumClazz).stream() ~~
        return Arrays.stream(enumClazz.getEnumConstants())
            .filter(enumCodeType -> enumCodeType.getCode() == enumCode)
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 공통 코드. " + enumCode + " in " + enumClazz.getName()));
    }

    public static <T extends Enum<T> & EnumCodeType> int getCodeFromEnumCodeType(T enumCodeType){
        return enumCodeType.getCode();
    }
}
