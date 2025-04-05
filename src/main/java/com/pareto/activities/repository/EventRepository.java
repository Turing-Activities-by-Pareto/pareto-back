package com.pareto.activities.repository;

import com.pareto.activities.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, String> {
    @Query("{ 'name': ?0, 'userId': ?1, 'title': ?2, 'description': ?3, 'category': ?4, 'subCategory': ?5, 'place': ?6, 'fileId': ?7 }")
    Page<EventEntity> query(
            Pageable pageable,
            String userId,
            String title,
            String description,
            String category,
            String subCategory,
            String place,
            String fileId
    );
}
