package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.ProductRequest;
import com.flatlogic.app.generator.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> getProducts(Integer offset, Integer limit, String orderBy);

    List<Product> getProducts(String query, Integer limit);

    Product getProductById(UUID id);

    Product saveProduct(ProductRequest productRequest);

    Product updateProduct(UUID id, ProductRequest productRequest);

    void deleteProduct(UUID id);

}
