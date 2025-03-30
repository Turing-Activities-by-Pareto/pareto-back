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
                EventCategoryEntity education = new EventCategoryEntity(0, "EDUCATION", "edu_bucket", "edu_object", null, null);
                EventCategoryEntity entertainment = new EventCategoryEntity(0, "ENTERTAINMENT", "ent_bucket", "ent_object", null, null);
                EventCategoryEntity intellectual = new EventCategoryEntity(0, "INTELLECTUAL", "int_bucket", "int_object", null, null);

                education = eventCategoryRepository.save(education);
                entertainment = eventCategoryRepository.save(entertainment);
                intellectual = eventCategoryRepository.save(intellectual);

                // Create Sub Categories
                List<SubEventCategoryEntity> subCategories = List.of(
                        new SubEventCategoryEntity(0, "HACKATHON", "edu_bucket", "hackathon_obj", education, null),
                        new SubEventCategoryEntity(0, "TECH-TALK (SEMINAR)", "edu_bucket", "tech_talk_obj", education, null),
                        new SubEventCategoryEntity(0, "WORKSHOP", "edu_bucket", "workshop_obj", education, null),
                        new SubEventCategoryEntity(0, "OTHER", "edu_bucket", "other_obj", education, null),

                        new SubEventCategoryEntity(0, "FOOTBALL", "ent_bucket", "football_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "VOLLEYBALL", "ent_bucket", "volleyball_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "BOWLING", "ent_bucket", "bowling_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "TRIPS", "ent_bucket", "trips_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "MAFIA", "ent_bucket", "mafia_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "BOARD GAMES", "ent_bucket", "board_games_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "PICNIC", "ent_bucket", "picnic_obj", entertainment, null),
                        new SubEventCategoryEntity(0, "OTHER", "ent_bucket", "other_obj", entertainment, null),

                        new SubEventCategoryEntity(0, "CHESS", "int_bucket", "chess_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "NHN", "int_bucket", "nhn_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "KHAMS", "int_bucket", "khams_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "KUDOS", "int_bucket", "kudos_obj", intellectual, null),
                        new SubEventCategoryEntity(0, "OTHER", "int_bucket", "other_obj", intellectual, null)
                );

                subEventCategoryRepository.saveAll(subCategories);
            }
        };
    }
}

