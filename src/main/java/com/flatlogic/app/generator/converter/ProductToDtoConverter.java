package com.flatlogic.app.generator.converter;

import com.flatlogic.app.generator.dto.CategoryDto;
import com.flatlogic.app.generator.dto.FileDto;
import com.flatlogic.app.generator.dto.ProductDto;
import com.flatlogic.app.generator.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductToDtoConverter implements Converter<Product, ProductDto> {

    private final CategoryToDtoConverter categoryToDtoConverter;
    private final FileToDtoConverter fileToDtoConverter;

    @Override
    public ProductDto convert(final Product source) {
        final var productDto = new ProductDto();
        productDto.setId(source.getId());
        productDto.setTitle(source.getTitle());
        productDto.setPrice(source.getPrice());
        productDto.setDiscount(source.getDiscount());
        productDto.setDescription(source.getDescription());
        productDto.setRating(source.getRating());
        Optional.ofNullable(source.getStatus()).ifPresent(
                status -> productDto.setStatus(status.getStatus()));
        productDto.setImportHash(source.getImportHash());
        Optional.ofNullable(source.getCategories()).ifPresent(categories -> {
            final List<CategoryDto> categoryDtos = productDto.getCategoryDtos();
            categories.forEach(category -> categoryDtos.add(categoryToDtoConverter.convert(category)));
        });
        Optional.ofNullable(source.getProducts()).ifPresent(products -> {
            final List<ProductDto> productDtos = productDto.getProductDtos();
            products.forEach(product -> productDtos.add(convertProductToDto(product)));
        });
        Optional.ofNullable(source.getFiles()).ifPresent(files -> {
            final List<FileDto> fileDtos = productDto.getFileDtos();
            files.forEach(file -> fileDtos.add(fileToDtoConverter.convert(file)));
        });
        productDto.setCreatedAt(source.getCreatedAt());
        productDto.setUpdatedAt(source.getUpdatedAt());
        productDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> productDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> productDto.setUpdatedById(user.getId()));
        return productDto;
    }

    private ProductDto convertProductToDto(final Product product) {
        final var productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setDescription(product.getDescription());
        productDto.setRating(product.getRating());
        Optional.ofNullable(product.getStatus()).ifPresent(
                status -> productDto.setStatus(status.getStatus()));
        productDto.setImportHash(product.getImportHash());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setUpdatedAt(product.getUpdatedAt());
        productDto.setDeletedAt(product.getDeletedAt());
        Optional.ofNullable(product.getCreatedBy()).ifPresent(
                user -> productDto.setCreatedById(user.getId()));
        Optional.ofNullable(product.getUpdatedBy()).ifPresent(
                user -> productDto.setUpdatedById(user.getId()));
        return productDto;
    }

}
