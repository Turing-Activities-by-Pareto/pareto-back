package com.pareto.activities.config;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import com.pareto.activities.entity.UserEntity;
import com.pareto.activities.enums.EParticipantCategory;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventSubCategoryRepository;
import com.pareto.activities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DBInit {

    private final EventCategoryRepository categoryRepository;
    private final EventSubCategoryRepository subCategoryRepository;
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (categoryRepository.count() == 0 && subCategoryRepository.count() == 0 && userRepository.count() == 0) {
                UserEntity user = UserEntity.builder()
                        .role(EParticipantCategory.STUDENT)
                        .id("67f0524490acbe11b019891f") //static id, so it wont generate everytime
                        .username("forever_student")
                        .password("123456")
                        .isActive(true)
                        .build();
                userRepository.save(user);

                Map<String, Set<String>> categoryMap = new HashMap<>();
                categoryMap.put("ENTERTAINMENT", Set.of("FOOTBALL", "VOLLEYBALL", "BOWLING", "TRIPS", "MAFIA", "BOARD GAMES", "PICNIC", "OTHER"));
                categoryMap.put("INTELLECTUAL", Set.of("CHESS", "NHN", "KHAMSA", "KUDOS", "OTHER"));
                categoryMap.put("EDUCATION", Set.of("HACKATHON", "TECH-TALK", "WORKSHOP", "OTHER"));
                categoryMap.put("OTHER", Set.of("OTHER"));

                categoryMap.forEach((categoryName, subCategoryNames) -> {
                    EventCategoryEntity category = EventCategoryEntity.builder()
                            .name(categoryName)
                            .build();
                    category = categoryRepository.save(category);
                    String categoryId = category.getId();


                    subCategoryNames.forEach(subCategoryName -> {
                        EventSubCategoryEntity subCategory = EventSubCategoryEntity.builder()
                                .name(subCategoryName)
                                .categoryId(categoryId)
                                .build();
                        subCategoryRepository.save(subCategory);
                    });
                });
            }
        };
    }
}