package com.flatlogic.app.ecomerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.controller.request.CategoryRequest;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.entity.Category;
import com.flatlogic.app.ecomerce.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@WithMockUser
class CategoryControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testGetCategories() throws Exception {
        List<Category> categories = getObjectFromJson("json/categories.json", new TypeReference<>() {
        });

        given(categoryService.getCategories(anyInt(), anyInt(), anyString())).willReturn(categories);

        mockMvc.perform(get("/categories")
                        .param("filter", "")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("orderBy", "")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows.*", hasSize(categories.size())))
                .andExpect(jsonPath("$.count", is(categories.size())));

        verify(categoryService).getCategories(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetCategoriesAutocompletes() throws Exception {
        List<Category> categories = getObjectFromJson("json/categories_autocomplete.json", new TypeReference<>() {
        });

        given(categoryService.getCategories(anyString(), anyInt())).willReturn(categories);

        mockMvc.perform(get("/categories/autocomplete")
                        .param("query", "c")
                        .param("limit", "10")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(categories.size())));

        verify(categoryService).getCategories(anyString(), anyInt());
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = getObjectFromJson("json/category.json", Category.class);

        given(categoryService.getCategoryById(any(UUID.class))).willReturn(category);

        mockMvc.perform(get("/categories/{id}", category.getId())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(category.getId().toString())))
                .andExpect(jsonPath("$.title", is(category.getTitle())));

        verify(categoryService).getCategoryById(any(UUID.class));
    }

    @Test
    void testSaveCategory() throws Exception {
        RequestData<CategoryRequest> requestData = getObjectFromJson("json/category_request.json", new TypeReference<>() {
        });
        Category category = getObjectFromJson("json/category.json", Category.class);

        given(categoryService.saveCategory(any(CategoryRequest.class))).willReturn(category);

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/categories/*"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title", is(category.getTitle())))
                .andExpect(jsonPath("$.importHash", is(category.getImportHash())));

        verify(categoryService).saveCategory(any(CategoryRequest.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        RequestData<CategoryRequest> requestData = getObjectFromJson("json/category_request.json", new TypeReference<>() {
        });
        Category category = getObjectFromJson("json/category.json", Category.class);

        given(categoryService.updateCategory(any(UUID.class), any(CategoryRequest.class))).willReturn(category);

        mockMvc.perform(put("/categories/{id}", category.getId())
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(category.getId().toString())))
                .andExpect(jsonPath("$.title", is(category.getTitle())))
                .andExpect(jsonPath("$.importHash", is(category.getImportHash())));

        verify(categoryService).updateCategory(any(UUID.class), any(CategoryRequest.class));
    }

    @Test
    void testDeleteCategory() throws Exception {
        willDoNothing().given(categoryService).deleteCategory(any(UUID.class));

        mockMvc.perform(delete("/categories/{id}", UUID.randomUUID())
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(any(UUID.class));
    }

}
