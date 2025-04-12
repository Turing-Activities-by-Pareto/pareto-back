package com.pareto.activities.repository;

import com.pareto.activities.entity.ParticipantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantCategoryRepository extends JpaRepository<ParticipantCategory, Long> {

    Optional<ParticipantCategory> findByName(String name);
}
