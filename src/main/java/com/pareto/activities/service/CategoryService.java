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
        List<EventCategoryEntity> categories = eventCategoryRepository.findAll();

        List<EventSubCategoryEntity> subCategories = eventSubCategoryRepository.findAll();

        Map<String, List<String>> categorySubCategoriesMap = new HashMap<>();
        for (EventCategoryEntity category : categories) {
            List<String> subCategoryNames = subCategories
                    .stream()
                    .filter(subCategory -> subCategory
                            .getCategoryId()
                            .equals(category.getId()))
                    .map(EventSubCategoryEntity::getName)
                    .collect(Collectors.toList())
                    ;

            categorySubCategoriesMap.put(
                    category.getName(),
                    subCategoryNames
            );
        }

        return categorySubCategoriesMap;
    }
}
