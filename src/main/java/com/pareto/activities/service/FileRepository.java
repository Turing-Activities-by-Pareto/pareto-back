package com.pareto.activities.service;

import com.pareto.activities.entity.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<FileEntity, Long> {
}
