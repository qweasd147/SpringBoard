package com.joo.repository;

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
    Page<BoardEntity> findAllWithFiles(@Param("state") int state, Pageable pageable);

    Optional<BoardEntity> findByIdxAndState(Long id, long boardState);
}
