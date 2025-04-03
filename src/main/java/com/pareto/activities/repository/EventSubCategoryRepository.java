package com.pareto.activities.repository;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventSubCategoryRepository extends JpaRepository<EventSubCategoryEntity, Long> {

    Optional<EventSubCategoryEntity> findByName(String name);

    List<EventSubCategoryEntity> findByCategory(EventCategoryEntity category);
}
