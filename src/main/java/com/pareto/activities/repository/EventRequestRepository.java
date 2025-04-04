package com.pareto.activities.repository;

import com.pareto.activities.entity.EventRequestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRequestRepository extends MongoRepository<EventRequestEntity, Long> {
}
