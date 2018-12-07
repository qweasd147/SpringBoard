package com.joo.repository;

import com.joo.model.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<FileEntity, Long> {
}
