package com.pareto.activities.repository;

import com.pareto.activities.entity.EventRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRequestRepository extends JpaRepository<EventRequestEntity, Long> {
}
