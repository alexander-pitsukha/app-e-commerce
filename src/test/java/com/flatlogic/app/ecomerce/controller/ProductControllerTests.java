package com.flatlogic.app.ecomerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.controller.request.ProductRequest;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.entity.Product;
import com.flatlogic.app.ecomerce.service.ProductService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@WithMockUser
class ProductControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testGetProducts() throws Exception {
        List<Product> products = getObjectFromJson("json/products.json", new TypeReference<>() {
        });

        given(productService.getProducts(anyInt(), anyInt(), anyString())).willReturn(products);

        mockMvc.perform(get("/products")
                        .param("filter", "")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("orderBy", "")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows.*", hasSize(products.size())))
                .andExpect(jsonPath("$.count", is(products.size())));

        verify(productService).getProducts(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetProductsAutocompletes() throws Exception {
        List<Product> products = getObjectFromJson("json/products_autocomplete.json", new TypeReference<>() {
        });

        given(productService.getProducts(anyString(), anyInt())).willReturn(products);

        mockMvc.perform(get("/products/autocomplete")
                        .param("query", "p")
                        .param("limit", "10")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(products.size())));

        verify(productService).getProducts(anyString(), anyInt());
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = getObjectFromJson("json/product.json", Product.class);

        given(productService.getProductById(any(UUID.class))).willReturn(product);

        mockMvc.perform(get("/products/{id}", product.getId())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().toString())))
                .andExpect(jsonPath("$.title", is(product.getTitle())))
                .andExpect(jsonPath("$.price", is(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.discount", is(product.getDiscount().doubleValue())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.rating", is(product.getRating())))
                .andExpect(jsonPath("$.status", is(product.getStatus().getStatus())))
                .andExpect(jsonPath("$.importHash", is(product.getImportHash())))
                .andExpect(jsonPath("$.categories.*", hasSize(product.getCategories().size())))
                .andExpect(jsonPath("$.more_products.*", hasSize(product.getProducts().size())))
                .andExpect(jsonPath("$.image.*", hasSize(product.getFiles().size())));

        verify(productService).getProductById(any(UUID.class));
    }

    @Test
    void testSaveProduct() throws Exception {
        RequestData<ProductRequest> requestData = getObjectFromJson("json/product_request.json", new TypeReference<>() {
        });
        Product product = getObjectFromJson("json/product.json", Product.class);

        given(productService.saveProduct(any(ProductRequest.class))).willReturn(product);

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/products/*"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title", is(product.getTitle())))
                .andExpect(jsonPath("$.price", is(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.discount", is(product.getDiscount().doubleValue())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.rating", is(product.getRating())))
                .andExpect(jsonPath("$.status", is(product.getStatus().getStatus())))
                .andExpect(jsonPath("$.importHash", is(product.getImportHash())))
                .andExpect(jsonPath("$.categories.*", hasSize(product.getCategories().size())))
                .andExpect(jsonPath("$.more_products.*", hasSize(product.getProducts().size())))
                .andExpect(jsonPath("$.image.*", hasSize(product.getFiles().size())));

        verify(productService).saveProduct(any(ProductRequest.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        RequestData<ProductRequest> requestData = getObjectFromJson("json/product_request.json", new TypeReference<>() {
        });
        Product product = getObjectFromJson("json/product.json", Product.class);

        given(productService.updateProduct(any(UUID.class), any(ProductRequest.class))).willReturn(product);

        mockMvc.perform(put("/products/{id}", product.getId())
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().toString())))
                .andExpect(jsonPath("$.title", is(product.getTitle())))
                .andExpect(jsonPath("$.price", is(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.discount", is(product.getDiscount().doubleValue())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.rating", is(product.getRating())))
                .andExpect(jsonPath("$.status", is(product.getStatus().getStatus())))
                .andExpect(jsonPath("$.importHash", is(product.getImportHash())))
                .andExpect(jsonPath("$.categories.*", hasSize(product.getCategories().size())))
                .andExpect(jsonPath("$.more_products.*", hasSize(product.getProducts().size())))
                .andExpect(jsonPath("$.image.*", hasSize(product.getFiles().size())));

        verify(productService).updateProduct(any(UUID.class), any(ProductRequest.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        willDoNothing().given(productService).deleteProduct(any(UUID.class));

        mockMvc.perform(delete("/products/{id}", UUID.randomUUID())
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(any(UUID.class));
    }

}
