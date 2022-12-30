package com.flatlogic.app.ecomerce.repositoty;

import com.flatlogic.app.ecomerce.entity.Category;
import com.flatlogic.app.ecomerce.repository.CategoryRepository;
import com.flatlogic.app.ecomerce.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
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
class CategoryRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testFindAllByDeletedAtIsNullPage() {
        List<Category> categories = categoryRepository.findAllByDeletedAtIsNull(Pageable.ofSize(10));

        assertEquals(4, categories.size());
    }

    @Test
    void testFindAllByDeletedAtIsNullSort() {
        List<Category> categories = categoryRepository
                .findAllByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());

        assertEquals(4, categories.size());
    }

    @Test
    void testFindAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc() {
        List<Category> categories = categoryRepository
                .findAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc("car",
                        PageRequest.of(0, 10, Sort.by(Constants.TITLE).ascending()));

        assertEquals(1, categories.size());
    }

    @Test
    void testUpdateDeletedAt() {
        UUID id = UUID.fromString("d160a5fe-1224-43f4-a05f-cbc60e97f2d5");

        categoryRepository.updateDeletedAt(id, LocalDateTime.now());
        Category category = categoryRepository.getById(id);

        assertNotNull(category.getId());
        assertNotNull(category.getDeletedAt());
    }

}
