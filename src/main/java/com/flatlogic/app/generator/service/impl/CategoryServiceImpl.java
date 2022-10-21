package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.CategoryRequest;
import com.flatlogic.app.generator.entity.Category;
import com.flatlogic.app.generator.exception.NoSuchEntityException;
import com.flatlogic.app.generator.repository.CategoryRepository;
import com.flatlogic.app.generator.service.CategoryService;
import com.flatlogic.app.generator.type.OrderType;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * CategoryService service.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageCodeUtil messageCodeUtil;

    /**
     * Get categories.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Categories
     */
    @Override
    public List<Category> getCategories(final Integer offset, final Integer limit, final String orderBy) {
        String[] orderParam = !ObjectUtils.isEmpty(orderBy) ? orderBy.split("_") : null;
        if (limit != null && offset != null) {
            var sort = Sort.by(Constants.CREATED_AT).descending();
            if (orderParam != null) {
                if (Objects.equals(OrderType.ASC.name(), orderParam[1])) {
                    sort = Sort.by(orderParam[0]).ascending();
                } else {
                    sort = Sort.by(orderParam[0]).descending();
                }
            }
            Pageable sortedByCreatedAtDesc = PageRequest.of(offset, limit, sort);
            return categoryRepository.findAllByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return categoryRepository.findAllByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get categories.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Categories
     */
    @Override
    public List<Category> getCategories(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return categoryRepository.findAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.TITLE).ascending()));
        } else {
            return categoryRepository.findAllByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.TITLE).ascending()));
        }
    }

    /**
     * Get category by id.
     *
     * @param id Category Id
     * @return Category
     */
    @Override
    public Category getCategoryById(final UUID id) {
        return categoryRepository.findById(id).orElse(null);
    }

    /**
     * Save category.
     *
     * @param categoryRequest CategoryData
     * @return Category
     */
    @Override
    @Transactional
    public Category saveCategory(final CategoryRequest categoryRequest) {
        var category = new Category();
        category.setTitle(categoryRequest.getTitle());
        category.setImportHash(categoryRequest.getImportHash());
        return categoryRepository.save(category);
    }

    /**
     * Update category.
     *
     * @param id              Category Id
     * @param categoryRequest CategoryData
     * @return Category
     */
    @Override
    @Transactional
    public Category updateCategory(final UUID id, final CategoryRequest categoryRequest) {
        var category = Optional.ofNullable(getCategoryById(id)).orElseThrow(() -> new NoSuchEntityException(
                messageCodeUtil.getFullErrorMessageByBundleCode(Constants.ERROR_MSG_CATEGORY_BY_ID_NOT_FOUND,
                        new Object[]{id})));
        category.setTitle(categoryRequest.getTitle());
        category.setImportHash(categoryRequest.getImportHash());
        return category;
    }

    /**
     * Delete category.
     *
     * @param id Category Id
     */
    @Override
    @Transactional
    public void deleteCategory(final UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_CATEGORY_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        categoryRepository.updateDeletedAt(id, LocalDateTime.now());
    }

}
