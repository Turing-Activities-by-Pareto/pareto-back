package com.pareto.activities.config;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import com.pareto.activities.entity.FileEntity;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventRepository;
import com.pareto.activities.repository.EventSubCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(
            EventCategoryRepository eventCategoryRepository,
            EventSubCategoryRepository subEventCategoryRepository,
            EventRepository eventRepository
    ) {
        return args -> {

            Map<String, String> strCategories = new HashMap<>();

            strCategories.put("ENTERTAINMENT", String.join(", ", Arrays.asList("FOOTBALL", "VOLLEYBALL", "BOWLING", "TRIPS", "MAFIA", "BOARD GAMES", "PICNIC", "OTHER")));
            strCategories.put("INTELLECTUAL", String.join(", ", Arrays.asList("CHESS", "NHN", "KHAMSA", "KUDOS", "OTHER")));
            strCategories.put("EDUCATION", String.join(", ", Arrays.asList("HACKATHON", "TECH-TALK", "WORKSHOP", "OTHER")));

            if (eventCategoryRepository.count() == 0) { // Prevent duplicate inserts

                // Create Event Categories
                EventCategoryEntity education = new EventCategoryEntity(
                        0,
                        "EDUCATION",
                        new FileEntity(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
                EventCategoryEntity entertainment = new EventCategoryEntity(
                        0,
                        "ENTERTAINMENT",
                        new FileEntity(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
                EventCategoryEntity intellectual = new EventCategoryEntity(
                        0,
                        "INTELLECTUAL",
                        new FileEntity(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );

                // Create Sub Categories
                List<EventSubCategoryEntity> educationSubCategories = List.of(
                        new EventSubCategoryEntity(
                                0,
                                "HACKATHON",
                                education,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "TECH-TALK",
                                education,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "WORKSHOP",
                                education,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "OTHER",
                                education,
                                new ArrayList<>()
                        )
                );

                List<EventSubCategoryEntity> entertainmentSubCategories = List.of(
                        new EventSubCategoryEntity(
                                0,
                                "FOOTBALL",
                                entertainment,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "VOLLEYBALL",
                                entertainment,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "BOWLING",
                                entertainment,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "TRIPS",
                                entertainment,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "MAFIA",
                                entertainment,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "BOARD GAMES",
                                entertainment,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "PICNIC",
                                entertainment,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "OTHER",
                                entertainment,
                                new ArrayList<>()
                        )
                );
                List<EventSubCategoryEntity> intellectualSubCategories = List.of(
                        new EventSubCategoryEntity(
                                0,
                                "CHESS",
                                intellectual,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "NHN",
                                intellectual,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "KHAMSA",
                                intellectual,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "KUDOS",
                                intellectual,
                                new ArrayList<>()
                        ),
                        new EventSubCategoryEntity(
                                0,
                                "OTHER",
                                intellectual,
                                new ArrayList<>()
                        )
                );

                education.addAllSubCategory(educationSubCategories);
                entertainment.addAllSubCategory(entertainmentSubCategories);
                intellectual.addAllSubCategory(intellectualSubCategories);

                eventCategoryRepository.save(education);
                eventCategoryRepository.save(entertainment);
                eventCategoryRepository.save(intellectual);
            }
        };
    }
}

