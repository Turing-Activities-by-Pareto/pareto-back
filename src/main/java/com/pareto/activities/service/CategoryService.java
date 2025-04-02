package com.pareto.activities.service;

import com.pareto.activities.DTO.CategoryResponse;
import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.SubEventCategoryEntity;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.SubEventCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final EventCategoryRepository eventCategoryRepository;
    private final SubEventCategoryRepository subEventCategoryRepository;

    public Map<String, List<String>> getAllWithSubCategories() {
        Map<String, List<String>> categories = new HashMap<>();

        List<EventCategoryEntity> all = eventCategoryRepository.findAll();

        for (EventCategoryEntity category : all) {
            categories.put(
                    category.getName(),
                    subEventCategoryRepository
                            .findByCategory(category)
                            .stream()
                            .map(SubEventCategoryEntity::getName)
                            .collect(Collectors.toList())
            );
        }

        return categories;
    }
}
