package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.controller.request.CategoryRequest;
import com.flatlogic.app.ecomerce.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> getCategories(Integer offset, Integer limit, String orderBy);

    List<Category> getCategories(String query, Integer limit);

    Category getCategoryById(UUID id);

    Category saveCategory(CategoryRequest categoryRequest);

    Category updateCategory(UUID id, CategoryRequest categoryRequest);

    void deleteCategory(UUID id);

}
