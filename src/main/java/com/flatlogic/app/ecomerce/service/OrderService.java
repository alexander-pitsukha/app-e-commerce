package com.flatlogic.app.ecomerce.service;

import com.flatlogic.app.ecomerce.controller.request.OrderRequest;
import com.flatlogic.app.ecomerce.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<Order> getOrders(Integer offset, Integer limit, String orderBy);

    Order getOrderById(UUID id);

    Order saveOrder(OrderRequest orderRequest);

    Order updateOrder(UUID id, OrderRequest orderRequest);

    void deleteOrder(UUID id);

}
