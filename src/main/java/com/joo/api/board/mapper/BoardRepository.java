package com.joo.api.board.mapper;

import com.joo.api.board.vo.BoardEsVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends ElasticsearchRepository<BoardEsVo, String> {

    Page<BoardEsVo> findBoardEsVosBySubjectAndContentsAndState(String subject, String contents, int state, Pageable pageable);

    BoardEsVo findBoardEsVoByIdx(int idx);
}
