package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.controller.data.AutocompleteData;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.controller.request.GetModelAttribute;
import com.flatlogic.app.ecomerce.controller.request.ProductRequest;
import com.flatlogic.app.ecomerce.controller.data.RowsData;
import com.flatlogic.app.ecomerce.dto.ProductDto;
import com.flatlogic.app.ecomerce.entity.Product;
import com.flatlogic.app.ecomerce.service.ProductService;
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
 * ProductController REST controller.
 */
@Tag(name = "Product controller", description = "Product resources that provides access to available product data")
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final DefaultConversionService defaultConversionService;

    /**
     * Get products.
     *
     * @param modelAttribute GetModelAttribute
     * @return Product RowsWrapper
     */
    @Operation(summary = "Get products", description = "Provides all available product list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RowsData<ProductDto>",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping
    public ResponseEntity<RowsData<ProductDto>> getProducts(@ModelAttribute GetModelAttribute modelAttribute) {
        log.info("Get products.");
        RowsData<ProductDto> rowsData = new RowsData<>();
        List<Product> products = productService.getProducts(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<ProductDto> productDtos = products.stream().map(product -> defaultConversionService.
                convert(product, ProductDto.class)).collect(Collectors.toList());
        rowsData.setRows(productDtos);
        rowsData.setCount(productDtos.size());
        return ResponseEntity.ok(rowsData);
    }

    /**
     * Get products.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Products
     */
    @Operation(summary = "Get products (autocomplete)",
            description = "Provides all available product list (autocomplete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RowsData<AutocompleteData>",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getProductsAutocomplete(@RequestParam String query,
                                                                          @RequestParam Integer limit) {
        log.info("Get products (autocomplete).");
        List<Product> products = productService.getProducts(query, limit);
        List<AutocompleteData> wrappers = products.stream().map(product ->
                new AutocompleteData(product.getId(), product.getTitle())).collect(Collectors.toList());
        return ResponseEntity.ok(wrappers);
    }

    /**
     * Get product by id.
     *
     * @param id Product Id
     * @return Product
     */
    @Operation(summary = "Get product by id", description = "Provides available product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return ProductDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        log.info("Get product by  id = {}.", id);
        return Optional.ofNullable(productService.getProductById(id))
                .map(product -> ResponseEntity.ok(defaultConversionService.convert(product, ProductDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save product.
     *
     * @param requestData RequestData
     * @return Product
     */
    @Operation(summary = "Save product", description = "Provides saving product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return ProductDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@RequestBody RequestData<ProductRequest> requestData) {
        log.info("Save product.");
        var product = productService.saveProduct(requestData.getData());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                        .buildAndExpand(product.getId()).toUri())
                .body(defaultConversionService.convert(product, ProductDto.class));
    }

    /**
     * Update product.
     *
     * @param id          Product id
     * @param requestData RequestData
     * @return Product
     */
    @Operation(summary = "Update product", description = "Provides updating product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return ProductDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id,
                                                    @RequestBody RequestData<ProductRequest> requestData) {
        log.info("Update product id = {}.", id);
        var product = productService.updateProduct(id, requestData.getData());
        return ResponseEntity.ok(defaultConversionService.convert(product, ProductDto.class));
    }

    /**
     * Delete product.
     *
     * @param id Product id
     * @return Void
     */
    @Operation(summary = "Delete product", description = "Provides deleting product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable UUID id) {
        log.info("Delete product id = {}.", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
