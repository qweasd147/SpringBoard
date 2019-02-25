package com.joo.repository;

import com.joo.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Transactional
    @Modifying
    @Query("update UserEntity userEntity SET userEntity.state = com.joo.common.state.CommonState.DELETE where userEntity.idx in :idxList")
    void deleteAllByIdInQuery(@Param("idxList") List<Long> idxList);

    UserEntity findByIdx(Long idx);

    UserEntity findByServiceNameAndId(String serviceName, String id);
}
