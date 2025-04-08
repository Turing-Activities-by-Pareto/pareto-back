package com.pareto.activities.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
class ApplicationConfigurationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testObjectMapperCustomDateTimeFormat() throws Exception {
        String dateTimeString = "2023-12-25T15:30:45";
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));

        String json = objectMapper.writeValueAsString(dateTime);
        LocalDateTime parsedDateTime = objectMapper.readValue(json, LocalDateTime.class);

        assertTrue(StringUtils.equals(parsedDateTime.toString(), dateTime.toString()));
        assertEquals(dateTime.toString(), parsedDateTime.toString());
    }
}