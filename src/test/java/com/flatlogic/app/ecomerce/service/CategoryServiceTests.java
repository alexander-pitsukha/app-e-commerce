package com.flatlogic.app.ecomerce.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.AppECommerceApplication;
import com.flatlogic.app.ecomerce.controller.request.CategoryRequest;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.entity.Category;
import com.flatlogic.app.ecomerce.util.WithCustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppECommerceApplication.class)
class CategoryServiceTests extends AbstractServiceTests {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testGetCategoriesThreeParamsIsNull() {
        List<Category> categories = categoryService.getCategories(null, null, null);

        assertEquals(4, categories.size());
    }

    @Test
    void testGetCategoriesThreeParamsIsNotNull() {
        List<Category> categories = categoryService.getCategories(1, 2, "title_DESC");

        assertEquals(2, categories.size());
    }

    @Test
    void testGetCategoriesTwoParamsIsNull() {
        List<Category> categories = categoryService.getCategories(null, 2);

        assertEquals(2, categories.size());
    }

    @Test
    void testGetCategoriesTwoParamsIsNotNull() {
        List<Category> categories = categoryService.getCategories("t", 2);

        assertEquals(1, categories.size());
    }

    @Test
    void testGetCategoryById() {
        Category category = categoryService.getCategoryById(UUID.fromString("d160a5fe-1224-43f4-a05f-cbc60e97f2d5"));

        assertNotNull(category);
    }

    @Test
    @WithCustomUserDetails
    void testSaveCategory() throws IOException {
        RequestData<CategoryRequest> requestData = getObjectFromJson("json/category_request.json", new TypeReference<>() {
        });

        Category category = categoryService.saveCategory(requestData.getData());

        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(requestData.getData().getTitle(), category.getTitle());
    }

    @Test
    void testUpdateCategory() throws IOException {
        RequestData<CategoryRequest> requestData = getObjectFromJson("json/category_request.json", new TypeReference<>() {
        });

        Category category = categoryService.updateCategory(UUID.fromString("d160a5fe-1224-43f4-a05f-cbc60e97f2d5"), requestData.getData());

        assertNotNull(category);
        assertEquals(requestData.getData().getTitle(), category.getTitle());
    }

    @Test
    void testDeleteCategory() {
        UUID uuid = UUID.fromString("d160a5fe-1224-43f4-a05f-cbc60e97f2d5");

        categoryService.deleteCategory(uuid);
        Category category = categoryService.getCategoryById(uuid);

        assertNotNull(category.getDeletedAt());
    }

}
