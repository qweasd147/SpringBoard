package com.joo.board;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestUtils {

    private static String GETTER_PREFIX = "get";

    public static MockMultipartHttpServletRequestBuilder addParamFromDto(
            MockMultipartHttpServletRequestBuilder requestBuilder, Object dto) {

        Class<?> target = dto.getClass();

        Arrays.stream(target.getDeclaredFields())
                .map((field) -> {
                    String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(field.getName());
                    return ReflectionUtils.findMethod(target, getterMethodName);
                })
                .filter(Objects::nonNull)
                .filter((method -> !method.isAccessible()))
                .filter((method)->{
                    String type = method.getReturnType().getName();
                    return "java.lang.String".equals(type);
                })
                .forEach((method)->{

                    String fieldName = StringUtils.uncapitalize(method.getName()
                            .replaceFirst(GETTER_PREFIX, ""));

                    try {
                        requestBuilder.param(fieldName, (String) method.invoke(dto));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

        return requestBuilder;
    }

    public static String dtoToQueryStr(Object dto, List<String> ignoreField){
        Class<?> target = dto.getClass();

        String queryStr = Arrays.stream(target.getDeclaredFields())
                .filter(field -> !field.isAccessible())                     //기본적으로 private만 값을 가져옴
                .filter(field -> !ignoreField.contains(field.getName()))    //무시할 프로퍼티 filter
                .filter(field -> {                                          //getter 메소드가 존재하는 프로퍼티만 filter
                    String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(field.getName());
                    return ReflectionUtils.findMethod(target, getterMethodName) != null;
                })
                .map(field -> {
                    String value = ReflectionTestUtils.invokeGetterMethod(dto, field.getName()).toString();
                    return Pair.of(field.getName(), value);
                })
                .filter(pair -> Objects.nonNull(pair.getValue()))
                .map(pair -> pair.getKey() + "=" + pair.getValue())
                .collect(Collectors.joining("&"));

        return "?" + queryStr;
    }
}
