package com.pareto.activities.repository;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventSubCategoryRepository extends MongoRepository<EventSubCategoryEntity, String> {

    boolean existsByName(String name);

    List<EventSubCategoryEntity> findByCategoryIdIn(Set<String> categoryIds);

    Optional<EventSubCategoryEntity> findByName(String name);

    List<EventSubCategoryEntity> findByCategoryId(String categoryId);

    List<EventSubCategoryEntity> findByIdIn(Set<String> subCategoryIds);
}