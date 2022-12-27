package com.flatlogic.app.ecomerce.service.impl;

import com.flatlogic.app.ecomerce.controller.request.ProductRequest;
import com.flatlogic.app.ecomerce.entity.Category;
import com.flatlogic.app.ecomerce.entity.File;
import com.flatlogic.app.ecomerce.entity.Product;
import com.flatlogic.app.ecomerce.exception.NoSuchEntityException;
import com.flatlogic.app.ecomerce.repository.CategoryRepository;
import com.flatlogic.app.ecomerce.repository.OrderRepository;
import com.flatlogic.app.ecomerce.repository.ProductRepository;
import com.flatlogic.app.ecomerce.service.ProductService;
import com.flatlogic.app.ecomerce.type.BelongsToColumnType;
import com.flatlogic.app.ecomerce.type.BelongsToType;
import com.flatlogic.app.ecomerce.type.OrderType;
import com.flatlogic.app.ecomerce.type.ProductStatusType;
import com.flatlogic.app.ecomerce.util.Constants;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ProductService service.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MessageCodeUtil messageCodeUtil;
    private final OrderRepository orderRepository;

    /**
     * Get products.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Products
     */
    @Override
    public List<Product> getProducts(final Integer offset, final Integer limit, final String orderBy) {
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
            return productRepository.findAllByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return productRepository.findAllByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get products.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Products
     */
    @Override
    public List<Product> getProducts(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return productRepository.findAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.TITLE).ascending()));
        } else {
            return productRepository.findAllByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.TITLE).ascending()));
        }
    }

    /**
     * Get product by id.
     *
     * @param id Product Id
     * @return Product
     */
    @Override
    public Product getProductById(final UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Save product.
     *
     * @param productRequest ProductData
     * @return Product
     */
    @Override
    @Transactional
    public Product saveProduct(final ProductRequest productRequest) {
        var product = new Product();
        setFields(productRequest, product);
        product = productRepository.save(product);
        setEntries(productRequest, product);
        return product;
    }

    /**
     * Update product.
     *
     * @param id             Product Id
     * @param productRequest ProductData
     * @return Product
     */
    @Override
    @Transactional
    public Product updateProduct(final UUID id, final ProductRequest productRequest) {
        var product = Optional.ofNullable(getProductById(id)).orElseThrow(() -> new NoSuchEntityException(
                messageCodeUtil.getFullErrorMessageByBundleCode(Constants.ERROR_MSG_PRODUCT_BY_ID_NOT_FOUND,
                        new Object[]{id})));
        setFields(productRequest, product);
        setEntries(productRequest, product);
        return product;
    }

    /**
     * Delete product.
     *
     * @param id Product Id
     */
    @Override
    @Transactional
    public void deleteProduct(final UUID id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        productRepository.updateDeletedAt(id, LocalDateTime.now());
        orderRepository.updateProductIdAtNull(id);
    }

    private void setFields(final ProductRequest productRequest, final Product product) {
        product.setTitle(productRequest.getTitle());
        product.setPrice(productRequest.getPrice());
        product.setDiscount(productRequest.getDiscount());
        product.setDescription(productRequest.getDescription());
        product.setRating(productRequest.getRating());
        product.setStatus(ProductStatusType.valueOfStatus(productRequest.getStatus()));
        product.setImportHash(productRequest.getImportHash());
    }

    private void setEntries(final ProductRequest productRequest, final Product product) {
        Optional.ofNullable(productRequest.getCategoryIds()).ifPresent(categoryIds -> {
            final List<Category> categories = product.getCategories();
            categories.clear();
            categoryIds.forEach(categoryId -> categories.add(categoryRepository.getById(categoryId)));
        });
        Optional.ofNullable(productRequest.getProductIds()).ifPresent(productIds -> {
            final List<Product> products = product.getProducts();
            products.clear();
            productIds.forEach(productId -> products.add(productRepository.getById(productId)));
        });
        Optional.ofNullable(productRequest.getFileRequests()).ifPresent(fileRequests -> {
            final List<File> files = product.getFiles();
            Map<UUID, File> mapFiles = files.stream().collect(Collectors.toMap(File::getId, file -> file));
            files.clear();
            fileRequests.forEach(fileRequest -> {
                File file = null;
                if (fileRequest.isNew()) {
                    file = new File();
                    file.setBelongsTo(BelongsToType.PRODUCTS.getType());
                    file.setBelongsToId(product.getId());
                    file.setBelongsToColumn(BelongsToColumnType.IMAGE.getType());
                    file.setName(fileRequest.getName());
                    file.setPrivateUrl(fileRequest.getPrivateUrl());
                    file.setPublicUrl(fileRequest.getPublicUrl());
                    file.setSizeInBytes(fileRequest.getSizeInBytes());
                } else {
                    file = mapFiles.remove(fileRequest.getId());
                }
                files.add(file);
            });
            mapFiles.forEach((key, value) -> {
                value.setDeletedAt(LocalDateTime.now());
                files.add(value);
            });
        });
    }

}
