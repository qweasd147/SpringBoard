package com.joo.service;

import org.springframework.util.StringUtils;

public class BaseService {

    public static String getJpaContainsLikePattern(String searchTerm){

        if(StringUtils.isEmpty(searchTerm)){
            return "%";
        }else{
            return "%" + searchTerm + "%";
        }
    }
}
