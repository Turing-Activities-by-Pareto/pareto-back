package com.pareto.activities.config;

import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.SubEventCategoryEntity;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.SubEventCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(EventCategoryRepository eventCategoryRepository,
                                      SubEventCategoryRepository subEventCategoryRepository) {
        return args -> {
            if (eventCategoryRepository.count() == 0) { // Prevent duplicate inserts

                // Create Event Categories
                EventCategoryEntity education = new EventCategoryEntity(0, "EDUCATION", "category-icon", "edu_object", null, null);
                EventCategoryEntity entertainment = new EventCategoryEntity(0, "ENTERTAINMENT", "category-icon", "ent_object", null, null);
                EventCategoryEntity intellectual = new EventCategoryEntity(0, "INTELLECTUAL", "category-icon", "int_object", null, null);

                education = eventCategoryRepository.save(education);
                entertainment = eventCategoryRepository.save(entertainment);
                intellectual = eventCategoryRepository.save(intellectual);

                // Create Sub Categories
                List<SubEventCategoryEntity> subCategories = List.of(
                        new SubEventCategoryEntity(0, "HACKATHON", "sub-category-icon", "hackathon_obj", education, null),
                        new SubEventCategoryEntity(0, "TECH-TALK", "sub-category-icon", "tech_talk_obj", education, null),
                        new SubEventCategoryEntity(0, "WORKSHOP", "sub-category-icon", "workshop_obj", education, null),
                        new SubEventCategoryEntity(0, "OTHER", "sub-category-icon", "other_obj", education, null),

                        new SubEventCategoryEntity(0, "FOOTBALL", "sub-category-icon", "football_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "VOLLEYBALL", "sub-category-icon", "volleyball_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "BOWLING", "sub-category-icon", "bowling_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "TRIPS", "sub-category-icon", "trips_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "MAFIA", "sub-category-icon", "mafia_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "BOARD GAMES", "sub-category-icon", "board_games_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "PICNIC", "sub-category-icon", "picnic_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "OTHER", "sub-category-icon", "other_obj", entertainment, null),

                        new SubEventCategoryEntity(0, "CHESS", "sub-category-icon", "chess_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "NHN", "sub-category-icon", "nhn_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "KHAMS", "sub-category-icon", "khams_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "KUDOS", "sub-category-icon", "kudos_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "OTHER", "sub-category-icon", "other_obj", intellectual, null)
                );

                subEventCategoryRepository.saveAll(subCategories);
            }
        };
    }
}

