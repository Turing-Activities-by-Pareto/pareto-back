package com.pareto.activities.repository;

import com.pareto.activities.entity.EventCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategoryEntity, Long> {
    EventCategoryEntity findByName(String category);
}
