package com.flatlogic.app.ecomerce.converter;

import com.flatlogic.app.ecomerce.AbstractTests;
import com.flatlogic.app.ecomerce.dto.OrderDto;
import com.flatlogic.app.ecomerce.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class OrderToDtoConverterTests extends AbstractTests {

    @Autowired
    private OrderToDtoConverter converter;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public OrderToDtoConverter dtoToFormConverter() {
            FileToDtoConverter fileToDtoConverter = new FileToDtoConverter();
            ProductToDtoConverter productToDtoConverter = new ProductToDtoConverter(new CategoryToDtoConverter(), fileToDtoConverter);
            return new OrderToDtoConverter(productToDtoConverter, new UserToDtoConverter(productToDtoConverter, fileToDtoConverter));
        }
    }

    @Test
    void testConvert() throws IOException {
        Order order = getObjectFromJson("json/order.json", Order.class);

        OrderDto orderDto = converter.convert(order);

        assertNotNull(orderDto);
        assertNotNull(orderDto.getId());

        assertEquals(order.getOrderDate(), orderDto.getOrderDate());
        assertEquals(order.getAmount(), orderDto.getAmount());
        assertEquals(order.getStatus().getStatus(), orderDto.getStatus());
        assertEquals(order.getImportHash(), orderDto.getImportHash());
        assertEquals(order.getProduct().getId(), orderDto.getProductId());
        assertEquals(order.getUser().getId(), orderDto.getUserId());
        assertEquals(order.getCreatedAt(), orderDto.getCreatedAt());
        assertEquals(order.getUpdatedAt(), orderDto.getUpdatedAt());
        assertEquals(order.getDeletedAt(), orderDto.getDeletedAt());
        assertEquals(order.getCreatedBy().getId(), orderDto.getCreatedById());
        assertEquals(order.getUpdatedBy().getId(), orderDto.getUpdatedById());

        assertProduct(order, orderDto);

        assertUser(order, orderDto);
    }

    private static void assertProduct(Order order, OrderDto orderDto) {
        assertEquals(order.getProduct().getId(), orderDto.getProductDto().getId());
        assertEquals(order.getProduct().getTitle(), orderDto.getProductDto().getTitle());
        assertEquals(order.getProduct().getPrice(), orderDto.getProductDto().getPrice());
        assertEquals(order.getProduct().getDiscount(), orderDto.getProductDto().getDiscount());
        assertEquals(order.getProduct().getDescription(), orderDto.getProductDto().getDescription());
        assertEquals(order.getProduct().getRating(), orderDto.getProductDto().getRating());
        assertEquals(order.getProduct().getStatus().getStatus(), orderDto.getProductDto().getStatus());
        assertEquals(order.getProduct().getImportHash(), orderDto.getProductDto().getImportHash());
        assertEquals(order.getProduct().getProducts().size(), orderDto.getProductDto().getProductDtos().size());
    }

    private static void assertUser(Order order, OrderDto orderDto) {
        assertEquals(order.getUser().getId(), orderDto.getUserDto().getId());
        assertEquals(order.getUser().getFirstName(), orderDto.getUserDto().getFirstName());
        assertEquals(order.getUser().getLastName(), orderDto.getUserDto().getLastName());
        assertEquals(order.getUser().getEmail(), orderDto.getUserDto().getEmail());
        assertEquals(order.getUser().getRole().getAuthority(), orderDto.getUserDto().getRole());
    }

}
