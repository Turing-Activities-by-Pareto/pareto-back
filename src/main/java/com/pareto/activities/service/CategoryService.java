package com.pareto.activities.service;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventSubCategoryRepository;
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
    private final EventSubCategoryRepository eventSubCategoryRepository;

    public Map<String, List<String>> getAllWithSubCategories() {
        Map<String, List<String>> categories = new HashMap<>();

        List<EventCategoryEntity> all = eventCategoryRepository.findAll();

        for (EventCategoryEntity category : all) {
            categories.put(
                    category.getName(),
                    eventSubCategoryRepository
                            .findByCategory(category)
                            .stream()
                            .map(EventSubCategoryEntity::getName)
                            .collect(Collectors.toList())
            );
        }

        return categories;
    }

    public Map<String, String> subCategoriesAlongsideCategory() {
        Map<String, String> categories = new HashMap<>();

        List<EventSubCategoryEntity> all = eventSubCategoryRepository.findAll();

        for (EventSubCategoryEntity subCategory : all) {
            categories.put(
                    subCategory.getName(),
                    subCategory.getCategory().getName()
            );
        }

        return categories;
    }
}
