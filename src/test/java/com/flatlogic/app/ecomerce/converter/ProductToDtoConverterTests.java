package com.flatlogic.app.ecomerce.converter;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.dto.ProductDto;
import com.flatlogic.app.ecomerce.entity.Product;
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
class ProductToDtoConverterTests extends AbstractTests {

    @Autowired
    private ProductToDtoConverter converter;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public ProductToDtoConverter dtoToFormConverter() {
            return new ProductToDtoConverter(new CategoryToDtoConverter(), new FileToDtoConverter());
        }
    }

    @Test
    void testConvert() throws IOException {
        Product product = getObjectFromJson("json/product.json", Product.class);

        ProductDto productDto = converter.convert(product);

        assertNotNull(productDto);
        assertNotNull(productDto.getId());

        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getTitle(), productDto.getTitle());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getDiscount(), productDto.getDiscount());
        assertEquals(product.getDescription(), productDto.getDescription());
        assertEquals(product.getRating(), productDto.getRating());
        assertEquals(product.getStatus().getStatus(), productDto.getStatus());
        assertEquals(product.getImportHash(), productDto.getImportHash());
        assertEquals(product.getCreatedAt(), productDto.getCreatedAt());
        assertEquals(product.getUpdatedAt(), productDto.getUpdatedAt());
        assertEquals(product.getDeletedAt(), productDto.getDeletedAt());
        assertEquals(product.getCategories().size(), productDto.getCategoryDtos().size());
        assertEquals(product.getProducts().size(), productDto.getProductDtos().size());
        assertEquals(product.getFiles().size(), productDto.getFileDtos().size());

        assertEquals(product.getCreatedBy().getId(), productDto.getCreatedById());
        assertEquals(product.getUpdatedBy().getId(), productDto.getUpdatedById());
    }

}
