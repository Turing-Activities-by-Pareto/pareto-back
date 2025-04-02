package com.pareto.activities.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                true
        );
        objectMapper.configure(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                true
        );
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                false
        );
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    //    @Bean
    public ObjectMapper objectMapperCamelCase() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false
        );
        objectMapper.configure(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                false
        );
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                false
        );
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.LowerCamelCaseStrategy());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
