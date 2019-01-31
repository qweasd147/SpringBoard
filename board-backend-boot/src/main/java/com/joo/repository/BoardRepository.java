package com.joo.repository;

import com.joo.common.state.CommonState;
import com.joo.model.dto.limited.LimitedBoard;
import com.joo.model.entity.BoardEntity;
import com.joo.repository.dsl.BoardRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, JpaSpecificationExecutor<BoardEntity>, BoardRepositoryCustom {

    @Modifying
    @Query("UPDATE BoardEntity board SET board.hits = board.hits+1 WHERE board.idx = :idx")
    void incrementHits(@Param("idx")Long idx);

    @Query(value = "SELECT board FROM BoardEntity board LEFT JOIN FETCH board.fileList " +
            "WHERE board.state = :state " +
            "ORDER BY board.idx desc",
            countQuery="SELECT count(board) FROM BoardEntity board WHERE board.state = :state"
    )
    Page<BoardEntity> findAllWithFiles(@Param("state") CommonState state, Pageable pageable);

    //interface로 잘 받아지나 테스트 + FETCH랑 비교
    @Query("SELECT boardEntity " +
            "FROM BoardEntity boardEntity " +
                "LEFT JOIN boardEntity.fileList fileEntity " +
                    "ON (boardEntity.idx = fileEntity.boardEntity) AND fileEntity.state = com.joo.common.state.CommonState.ENABLE " +
            "WHERE boardEntity.idx = :boardIdx AND boardEntity.state = com.joo.common.state.CommonState.ENABLE"
    )
    Optional<LimitedBoard> findEnableBoardByBoardIdx_(@Param("boardIdx")Long boardIdx);

    @Query("SELECT boardEntity " +
            "FROM BoardEntity boardEntity " +
                "JOIN FETCH boardEntity.fileList fileEntity " +
            "WHERE boardEntity.idx = :boardIdx " +
                "AND boardEntity.state = com.joo.common.state.CommonState.ENABLE " +
                "AND fileEntity.state = com.joo.common.state.CommonState.ENABLE"
    )
    Optional<BoardEntity> findEnableBoardByBoardIdx(@Param("boardIdx")Long boardIdx);
}
