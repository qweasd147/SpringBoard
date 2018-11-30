package com.joo.model.entity;

import com.joo.model.BaseModel;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<T> extends BaseModel{

    @CreatedBy
    private T createdBy;

    @CreatedDate
    private LocalDateTime createdDate;

    private T lastModifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
