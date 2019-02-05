package com.joo.common.converter;

import com.joo.common.state.CommonState;

import javax.persistence.AttributeConverter;

/**
 * 공통 추상 convert를 구현하는 형태로 바꿔서 Deprecated
 */
@Deprecated
public class ConvertToCommonState implements AttributeConverter<CommonState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CommonState attribute) {
        return EnumConvertUtils.getCodeFromEnumCodeType(attribute);
    }

    @Override
    public CommonState convertToEntityAttribute(Integer dbData) {
        return EnumConvertUtils.getEnumCodeTypeByCode(CommonState.class, dbData);
    }
}
