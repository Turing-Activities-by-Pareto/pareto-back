package com.pareto.activities.repository;

import com.pareto.activities.entity.EventCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategoryEntity, Long> {
    Optional<EventCategoryEntity> findByName(String category);
}
