package com.flatlogic.app.ecomerce;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatlogic.app.ecomerce.configuration.TestAppConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;

@Getter
@Import(TestAppConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class AbstractTests {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T getObjectFromJson(String fileSource, Class<T> valueType) throws java.io.IOException {
        return objectMapper.readValue(new ClassPathResource(fileSource).getInputStream(), valueType);
    }

    public <T> T getObjectFromJson(String fileSource, TypeReference<T> valueTypeRef) throws java.io.IOException {
        return objectMapper.readValue(new ClassPathResource(fileSource).getInputStream(), valueTypeRef);
    }

}
