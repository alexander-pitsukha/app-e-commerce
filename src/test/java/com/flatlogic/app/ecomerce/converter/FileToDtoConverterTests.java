package com.flatlogic.app.ecomerce.converter;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.dto.FileDto;
import com.flatlogic.app.ecomerce.entity.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class FileToDtoConverterTests extends AbstractTests {

    @Autowired
    private FileToDtoConverter converter;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public FileToDtoConverter dtoToFormConverter() {
            return new FileToDtoConverter();
        }
    }

    @Test
    void testConvert() throws IOException {

        File file = getObjectFromJson("json/file.json", File.class);

        FileDto fileDto = converter.convert(file);

        assertNotNull(fileDto);
        assertNotNull(fileDto.getId());

        assertEquals(file.getId(), fileDto.getId());
        assertEquals(file.getBelongsTo(), fileDto.getBelongsTo());
        assertEquals(file.getBelongsToId(), fileDto.getBelongsToId());
        assertEquals(file.getBelongsToColumn(), fileDto.getBelongsToColumn());
        assertEquals(file.getName(), fileDto.getName());
        assertEquals(file.getSizeInBytes(), fileDto.getSizeInBytes());
        assertEquals(file.getPrivateUrl(), fileDto.getPrivateUrl());
        assertEquals(file.getPublicUrl(), fileDto.getPublicUrl());
        assertEquals(file.getCreatedAt(), fileDto.getCreatedAt());
        assertEquals(file.getUpdatedAt(), fileDto.getUpdatedAt());
        assertEquals(file.getDeletedAt(), fileDto.getDeletedAt());

        assertEquals(file.getCreatedBy().getId(), fileDto.getCreatedById());
        assertEquals(file.getUpdatedBy().getId(), fileDto.getUpdatedById());
    }

}
