package com.flatlogic.app.ecomerce.converter;

import com.flatlogic.app.ecomerce.dto.OrderDto;
import com.flatlogic.app.ecomerce.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderToDtoConverter implements Converter<Order, OrderDto> {

    private final ProductToDtoConverter productToDtoConverter;
    private final UserToDtoConverter userToDtoConverter;

    @Override
    public OrderDto convert(final Order source) {
        final var orderDto = new OrderDto();
        orderDto.setId(source.getId());
        orderDto.setOrderDate(source.getOrderDate());
        orderDto.setAmount(source.getAmount());
        Optional.ofNullable(source.getStatus()).ifPresent(
                status -> orderDto.setStatus(status.getStatus()));
        orderDto.setImportHash(source.getImportHash());
        Optional.ofNullable(source.getProduct()).ifPresent(product -> {
            orderDto.setProductId(product.getId());
            orderDto.setProductDto(productToDtoConverter.convert(product));
        });
        Optional.ofNullable(source.getUser()).ifPresent(user -> {
            orderDto.setUserId(user.getId());
            orderDto.setUserDto(userToDtoConverter.convert(user));
        });
        orderDto.setCreatedAt(source.getCreatedAt());
        orderDto.setUpdatedAt(source.getUpdatedAt());
        orderDto.setDeletedAt(source.getDeletedAt());
        Optional.ofNullable(source.getCreatedBy()).ifPresent(
                user -> orderDto.setCreatedById(user.getId()));
        Optional.ofNullable(source.getUpdatedBy()).ifPresent(
                user -> orderDto.setUpdatedById(user.getId()));
        return orderDto;
    }

}
