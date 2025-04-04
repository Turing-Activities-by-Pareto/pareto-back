package com.pareto.activities.repository;

import com.pareto.activities.entity.EventCategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventCategoryRepository extends MongoRepository<EventCategoryEntity, String> {
    Optional<EventCategoryEntity> findByName(String category);
}