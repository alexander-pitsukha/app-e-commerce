package com.flatlogic.app.ecomerce.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TestConfiguration
public class TestAppConfiguration {

    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Bean
    public ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter
                .ofPattern(LOCAL_DATE_TIME_FORMAT));
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter
                .ofPattern(LOCAL_DATE_TIME_FORMAT));
        javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        return Jackson2ObjectMapperBuilder.json().modules(javaTimeModule)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
    }

}
