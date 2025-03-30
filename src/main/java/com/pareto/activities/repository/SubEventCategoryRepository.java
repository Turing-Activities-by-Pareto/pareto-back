package com.pareto.activities.repository;

import com.pareto.activities.entity.SubEventCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubEventCategoryRepository extends JpaRepository<SubEventCategoryEntity, Long> {
    SubEventCategoryEntity findByName(String subCategory);
}
