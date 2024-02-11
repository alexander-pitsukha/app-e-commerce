package com.flatlogic.app.ecomerce.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.AppECommerceApplication;
import com.flatlogic.app.ecomerce.controller.request.ProductRequest;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppECommerceApplication.class)
class ProductServiceTests extends AbstractServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    void testGetProductsThreeParamsIsNull() {
        List<Product> products = productService.getProducts(null, null, null);

        assertEquals(2, products.size());
    }

    @Test
    void testGetProductsThreeParamsIsNotNull() {
        List<Product> products = productService.getProducts(0, 1, "title_DESC");

        assertEquals(1, products.size());
    }

    @Test
    void testGetProductsTwoParamsIsNull() {
        List<Product> products = productService.getProducts(null, 2);

        assertEquals(2, products.size());
    }

    @Test
    void testGetProductsTwoParamsIsNotNull() {
        List<Product> products = productService.getProducts("product1", 2);

        assertEquals(1, products.size());
    }

    @Test
    void testGetProductById() {
        Product product = productService.getProductById(UUID.fromString("0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4"));

        assertNotNull(product);
    }

    @Test
    void testSaveProduct() throws IOException {
        RequestData<ProductRequest> requestData = getObjectFromJson("json/product_request.json", new TypeReference<>() {
        });

        Product product = productService.saveProduct(requestData.getData());

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(requestData.getData().getTitle(), product.getTitle());
        assertEquals(requestData.getData().getPrice(), product.getPrice());
        assertEquals(requestData.getData().getDiscount(), product.getDiscount());
        assertEquals(requestData.getData().getDescription(), product.getDescription());
        assertEquals(requestData.getData().getRating(), product.getRating());
        assertEquals(requestData.getData().getStatus(), product.getStatus().getStatus());
        assertEquals(requestData.getData().getImportHash(), product.getImportHash());
        assertEquals(requestData.getData().getCategoryIds().size(), product.getCategories().size());
        assertEquals(requestData.getData().getProductIds().size(), product.getProducts().size());
        assertEquals(requestData.getData().getFileRequests().size(), product.getFiles().size());
    }

    @Test
    void testUpdateProduct() throws IOException {
        RequestData<ProductRequest> requestData = getObjectFromJson("json/product_request.json", new TypeReference<>() {
        });

        Product product = productService.updateProduct(UUID.fromString("6b2d3137-6edd-4201-a1c3-0624df6f704a"), requestData.getData());

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(requestData.getData().getTitle(), product.getTitle());
        assertEquals(requestData.getData().getPrice(), product.getPrice());
        assertEquals(requestData.getData().getDiscount(), product.getDiscount());
        assertEquals(requestData.getData().getDescription(), product.getDescription());
        assertEquals(requestData.getData().getRating(), product.getRating());
        assertEquals(requestData.getData().getStatus(), product.getStatus().getStatus());
        assertEquals(requestData.getData().getImportHash(), product.getImportHash());
        assertEquals(requestData.getData().getCategoryIds().size(), product.getCategories().size());
        assertEquals(requestData.getData().getProductIds().size(), product.getProducts().size());
        assertEquals(requestData.getData().getFileRequests().size(), product.getFiles().size());
    }

    @Test
    void testDeleteProduct() {
        UUID uuid = UUID.fromString("6b2d3137-6edd-4201-a1c3-0624df6f704a");

        productService.deleteProduct(uuid);
        Product product = productService.getProductById(uuid);

        assertNotNull(product.getDeletedAt());
    }

}
