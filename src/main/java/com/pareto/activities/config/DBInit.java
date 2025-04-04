package com.pareto.activities.config;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventSubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DBInit {

    private final EventCategoryRepository categoryRepository;
    private final EventSubCategoryRepository subCategoryRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (categoryRepository.count() == 0) {
                Map<String, Set<String>> categoryMap = new HashMap<>();
                categoryMap.put("ENTERTAINMENT", Set.of("FOOTBALL", "VOLLEYBALL", "BOWLING", "TRIPS", "MAFIA", "BOARD GAMES", "PICNIC", "OTHER"));
                categoryMap.put("INTELLECTUAL", Set.of("CHESS", "NHN", "KHAMSA", "KUDOS", "OTHER"));
                categoryMap.put("EDUCATION", Set.of("HACKATHON", "TECH-TALK", "WORKSHOP", "OTHER"));
                categoryMap.put("OTHER", Set.of("OTHER"));

                categoryMap.forEach((categoryName, subCategoryNames) -> {
                    Set<EventSubCategoryEntity> subCategories = new HashSet<>();
                    subCategoryNames.forEach(subCategoryName -> {
                        EventSubCategoryEntity subCategory = EventSubCategoryEntity.builder()
                                .name(subCategoryName)
                                .build();
                        subCategories.add(subCategoryRepository.save(subCategory));
                    });

                    EventCategoryEntity category = EventCategoryEntity.builder()
                            .name(categoryName)
                            .subCategories(subCategories)
                            .build();
                    categoryRepository.save(category);
                });
            }
        };
    }
}