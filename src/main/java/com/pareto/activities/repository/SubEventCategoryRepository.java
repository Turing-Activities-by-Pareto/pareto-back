package com.pareto.activities.repository;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.SubEventCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubEventCategoryRepository extends JpaRepository<SubEventCategoryEntity, Long> {

    Optional<SubEventCategoryEntity> findByName(String name);

    List<SubEventCategoryEntity> findByCategory(EventCategoryEntity category);
}
