package com.flatlogic.app.ecomerce.repositoty;

import com.flatlogic.app.ecomerce.entity.Product;
import com.flatlogic.app.ecomerce.repository.ProductRepository;
import com.flatlogic.app.ecomerce.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindAllByDeletedAtIsNullPage() {
        List<Product> products = productRepository.findAllByDeletedAtIsNull(Pageable.ofSize(10));

        assertEquals(2, products.size());
    }

    @Test
    void testFindAllByDeletedAtIsNullSort() {
        List<Product> products = productRepository.findAllByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());

        assertEquals(2, products.size());
    }

    @Test
    void testFindAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc() {
        List<Product> products = productRepository
                .findAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc("product1", Pageable.ofSize(10));

        assertEquals(1, products.size());
    }

    @Test
    void testUpdateDeletedAt() {
        UUID id = UUID.fromString("0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4");

        productRepository.updateDeletedAt(id, LocalDateTime.now());
        Product product = productRepository.getById(id);

        assertNotNull(product.getDeletedAt());
    }

}
