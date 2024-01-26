package com.flatlogic.app.ecomerce.converter;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.dto.CategoryDto;
import com.flatlogic.app.ecomerce.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CategoryToDtoConverterTests extends AbstractTests {

    @Autowired
    private CategoryToDtoConverter converter;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public CategoryToDtoConverter dtoToFormConverter() {
            return new CategoryToDtoConverter();
        }
    }
    
    @Test
    void testConvert() throws IOException {
        Category category = getObjectFromJson("json/category.json", Category.class);

        CategoryDto categoryDto = converter.convert(category);

        assertNotNull(categoryDto);
        assertNotNull(categoryDto.getId());
        assertEquals(category.getId(), categoryDto.getId());
        assertEquals(category.getTitle(), categoryDto.getTitle());
        assertEquals(category.getImportHash(), categoryDto.getImportHash());
        assertEquals(category.getCreatedAt(), categoryDto.getCreatedAt());
        assertEquals(category.getUpdatedAt(), categoryDto.getUpdatedAt());
        assertEquals(category.getDeletedAt(), categoryDto.getDeletedAt());
        assertEquals(category.getCreatedBy().getId(), categoryDto.getCreatedById());
        assertEquals(category.getUpdatedBy().getId(), categoryDto.getUpdatedById());
    }

}
