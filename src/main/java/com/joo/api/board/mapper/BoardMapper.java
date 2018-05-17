package com.joo.api.board.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository(value = "boardMapper")
public interface BoardMapper {

    public Map<String, ?> testValue();
}
