package com.joo.board;

import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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

    public static String dtoToQueryStr(Object dto){
        Class<?> target = dto.getClass();

        String queryStr = Arrays.stream(target.getDeclaredFields())
                .filter(field -> !field.isAccessible())
                .map(field -> {
                    try {
                        return field.getName() + "=" + field.get(dto);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.joining("&"));

        return "?" + queryStr;
    }
}
