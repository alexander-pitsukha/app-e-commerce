package com.flatlogic.app.ecomerce.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.AppECommerceApplication;
import com.flatlogic.app.ecomerce.controller.request.OrderRequest;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.entity.Order;
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
class OrderServiceTests extends AbstractServiceTests {

    @Autowired
    private OrderService orderService;

    @Test
    void testGetOrdersParamsIsNull() {
        List<Order> orders = orderService.getOrders(null, null, null);

        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrdersParamsIsNotNull() {
        List<Order> orders = orderService.getOrders(0, 1, "amount_DESC");

        assertEquals(1, orders.size());
    }

    @Test
    void testGetOrderById() {
        Order order = orderService.getOrderById(UUID.fromString("8050f33a-1903-4ac7-a7d1-a6c3a77834bd"));

        assertNotNull(order);
    }

    @Test
    void testSaveOrder() throws IOException {
        RequestData<OrderRequest> requestData = getObjectFromJson("json/order_request.json", new TypeReference<>() {
        });

        Order order = orderService.saveOrder(requestData.getData());

        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(requestData.getData().getOrderDate(), order.getOrderDate());
        assertEquals(requestData.getData().getAmount(), order.getAmount());
        assertEquals(requestData.getData().getStatus(), order.getStatus().getStatus());
        assertEquals(requestData.getData().getProductId(), order.getProduct().getId());
        assertEquals(requestData.getData().getUserId(), order.getUser().getId());
    }

    @Test
    void testUpdateOrder() throws IOException {
        RequestData<OrderRequest> requestData = getObjectFromJson("json/order_request.json", new TypeReference<>() {
        });

        Order order = orderService.updateOrder(UUID.fromString("6d2dd84a-f17f-4be8-8cb1-255aa88342c3"), requestData.getData());

        assertNotNull(order);
        assertEquals(requestData.getData().getOrderDate(), order.getOrderDate());
        assertEquals(requestData.getData().getAmount(), order.getAmount());
        assertEquals(requestData.getData().getStatus(), order.getStatus().getStatus());
        assertEquals(requestData.getData().getProductId(), order.getProduct().getId());
        assertEquals(requestData.getData().getUserId(), order.getUser().getId());
    }

    @Test
    void testDeleteOrder() {
        UUID uuid = UUID.fromString("6d2dd84a-f17f-4be8-8cb1-255aa88342c3");

        orderService.deleteOrder(uuid);
        Order order = orderService.getOrderById(uuid);

        assertNotNull(order.getDeletedAt());

    }

}
