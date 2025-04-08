package com.pareto.activities.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Configuration
public class ApplicationConfiguration {

    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        // formatter:off
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        // formatter:on

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(formatter)
        );
        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(formatter)
        );

        objectMapper.registerModule(javaTimeModule);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
                (Converter<LocalDateTime, Date>) source -> Date.from(source
                                                                             .atZone(ZoneId.systemDefault())
                                                                             .toInstant()),
                (Converter<Date, LocalDateTime>) source -> source
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        ));
    }
}
