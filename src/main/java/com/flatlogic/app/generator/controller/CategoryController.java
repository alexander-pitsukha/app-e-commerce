package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.CategoryRequest;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.CategoryDto;
import com.flatlogic.app.generator.entity.Category;
import com.flatlogic.app.generator.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CategoryController REST controller.
 */
@Tag(name = "Category controller", description = "Category resources that provides access to available category data")
@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final DefaultConversionService defaultConversionService;

    /**
     * Get categories.
     *
     * @param modelAttribute GetModelAttribute
     * @return Category RowsWrapper
     */
    @Operation(summary = "Get categories", description = "Provides all available category list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RowsData<CategoryDto>",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping
    public ResponseEntity<RowsData<CategoryDto>> getCategories(@ModelAttribute GetModelAttribute modelAttribute) {
        log.info("Get categories.");
        RowsData<CategoryDto> rowsData = new RowsData<>();
        List<Category> categories = categoryService.getCategories(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<CategoryDto> categoryDtos = categories.stream().map(category -> defaultConversionService.
                convert(category, CategoryDto.class)).collect(Collectors.toList());
        rowsData.setRows(categoryDtos);
        rowsData.setCount(categoryDtos.size());
        return ResponseEntity.ok(rowsData);
    }

    /**
     * Get categories.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Categories
     */
    @Operation(summary = "Get categories (autocomplete)",
            description = "Provides all available category list (autocomplete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RowsData<AutocompleteData>",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getCategoriesAutocomplete(@RequestParam String query,
                                                                            @RequestParam Integer limit) {
        log.info("Get categories (autocomplete).");
        List<Category> categories = categoryService.getCategories(query, limit);
        List<AutocompleteData> wrappers = categories.stream().map(category ->
                new AutocompleteData(category.getId(), category.getTitle())).collect(Collectors.toList());
        return ResponseEntity.ok(wrappers);
    }

    /**
     * Get category by id.
     *
     * @param id Category Id
     * @return Category
     */
    @Operation(summary = "Get category by id", description = "Provides available category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return CategoryDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        log.info("Get category by  id = {}.", id);
        return Optional.ofNullable(categoryService.getCategoryById(id))
                .map(category -> ResponseEntity.ok(defaultConversionService.convert(category, CategoryDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save category.
     *
     * @param requestData RequestData
     * @return Category
     */
    @Operation(summary = "Save category", description = "Provides saving category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return CategoryDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody RequestData<CategoryRequest> requestData) {
        log.info("Save category.");
        var category = categoryService.saveCategory(requestData.getData());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                        .buildAndExpand(category.getId()).toUri())
                .body(defaultConversionService.convert(category, CategoryDto.class));
    }

    /**
     * Update category.
     *
     * @param id          Category id
     * @param requestData RequestData
     * @return Category
     */
    @Operation(summary = "Update category", description = "Provides updating category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return CategoryDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id,
                                                      @RequestBody RequestData<CategoryRequest> requestData) {
        log.info("Update category id = {}.", id);
        var category = categoryService.updateCategory(id, requestData.getData());
        return ResponseEntity.ok(defaultConversionService.convert(category, CategoryDto.class));
    }

    /**
     * Delete category.
     *
     * @param id Category id
     * @return Void
     */
    @Operation(summary = "Delete category", description = "Provides deleting category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable UUID id) {
        log.info("Delete category id = {}.", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
