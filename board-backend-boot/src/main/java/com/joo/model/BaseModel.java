package com.joo.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BaseModel implements Serializable{

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static final ModelMapper MODEL_MAPPER = new ModelMapper();

    protected static final <T, R> R convertType(T obj, Class<R> clazz){
        return MODEL_MAPPER.map(obj, clazz);
    }

    protected static final <T, R> R convertType(T obj, Class<R> clazz, BiFunction<T, R, R> handleConvertAfter){
        R convertResult = convertType(obj, clazz);
        if(Objects.nonNull(handleConvertAfter))
            return handleConvertAfter.apply(obj, convertResult);
        else    return convertResult;
    }
}
