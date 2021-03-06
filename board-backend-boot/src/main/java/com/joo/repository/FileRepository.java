package com.joo.repository;

import com.joo.common.state.CommonState;
import com.joo.model.entity.FileEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends CrudRepository<FileEntity, Long> {

    @Transactional
    @Modifying
    @Query("update FileEntity fileEntity SET fileEntity.state = com.joo.common.state.CommonState.DELETE where fileEntity.idx in :idxList")
    void deleteAllByIdInQuery(@Param("idxList") List<Long> idxList);

    Optional<List<FileEntity>> findByBoardEntity_IdxAndState(Long board_idx, CommonState state);

    Optional<FileEntity> findByBoardEntity_IdxAndIdxAndState(Long board_idx, Long idx, CommonState state);
}
