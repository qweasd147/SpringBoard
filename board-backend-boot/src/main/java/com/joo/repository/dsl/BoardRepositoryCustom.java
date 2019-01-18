package com.joo.repository.dsl;

import com.joo.model.dto.BoardSearchDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.state.BoardState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardEntity> findAllDynamic(BoardSearchDto boardSearchDto, BoardState boardState, Pageable pageable);
}
