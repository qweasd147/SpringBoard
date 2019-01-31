package com.joo.repository.dsl;

import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardEntity> findAllDynamic(BoardSearchDto boardSearchDto, CommonState boardState, Pageable pageable);
}
