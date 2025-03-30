package com.pareto.activities.repository;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.SubEventCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubEventCategoryRepository extends JpaRepository<SubEventCategoryEntity, Long> {
    SubEventCategoryEntity findByName(String subCategory);

    List<SubEventCategoryEntity> findByCategory(EventCategoryEntity category);
}
