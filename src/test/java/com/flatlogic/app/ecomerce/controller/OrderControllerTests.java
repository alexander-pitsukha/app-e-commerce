package com.flatlogic.app.ecomerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flatlogic.app.ecomerce.controller.request.OrderRequest;
import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.entity.Order;
import com.flatlogic.app.ecomerce.service.OrderService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@WithMockUser
class OrderControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testGetOrders() throws Exception {
        List<Order> orders = getObjectFromJson("json/orders.json", new TypeReference<>() {
        });

        given(orderService.getOrders(anyInt(), anyInt(), anyString())).willReturn(orders);

        mockMvc.perform(get("/orders")
                        .param("filter", "")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("orderBy", "")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows.*", hasSize(orders.size())))
                .andExpect(jsonPath("$.count", is(orders.size())));

        verify(orderService).getOrders(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = getObjectFromJson("json/order.json", Order.class);

        given(orderService.getOrderById(any(UUID.class))).willReturn(order);

        mockMvc.perform(get("/orders/{id}", order.getId())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId().toString())))
                .andExpect(jsonPath("$.order_date", is(order.getOrderDate())))
                .andExpect(jsonPath("$.amount", is(order.getAmount())))
                .andExpect(jsonPath("$.status", is(order.getStatus().getStatus())))
                .andExpect(jsonPath("$.importHash", is(order.getImportHash())))
                .andExpect(jsonPath("$.product.id", is(order.getProduct().getId().toString())))
                .andExpect(jsonPath("$.product.title", is(order.getProduct().getTitle())))
                .andExpect(jsonPath("$.user.id", is(order.getUser().getId().toString())))
                .andExpect(jsonPath("$.user.email", is(order.getUser().getEmail())));

        verify(orderService).getOrderById(any(UUID.class));
    }

    @Test
    void testSaveOrder() throws Exception {
        RequestData<OrderRequest> requestData = getObjectFromJson("json/order_request.json", new TypeReference<>() {
        });
        Order order = getObjectFromJson("json/order.json", Order.class);

        given(orderService.saveOrder(any(OrderRequest.class))).willReturn(order);

        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/orders/*"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.order_date", is(order.getOrderDate())))
                .andExpect(jsonPath("$.amount", is(order.getAmount())))
                .andExpect(jsonPath("$.status", is(order.getStatus().getStatus())))
                .andExpect(jsonPath("$.importHash", is(order.getImportHash())))
                .andExpect(jsonPath("$.product.id", is(order.getProduct().getId().toString())))
                .andExpect(jsonPath("$.product.title", is(order.getProduct().getTitle())))
                .andExpect(jsonPath("$.user.id", is(order.getUser().getId().toString())))
                .andExpect(jsonPath("$.user.email", is(order.getUser().getEmail())));

        verify(orderService).saveOrder(any(OrderRequest.class));
    }

    @Test
    void testUpdateOrder() throws Exception {
        RequestData<OrderRequest> requestData = getObjectFromJson("json/order_request.json", new TypeReference<>() {
        });
        Order order = getObjectFromJson("json/order.json", Order.class);

        given(orderService.updateOrder(any(UUID.class), any(OrderRequest.class))).willReturn(order);

        mockMvc.perform(put("/orders/{id}", order.getId())
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId().toString())))
                .andExpect(jsonPath("$.order_date", is(order.getOrderDate())))
                .andExpect(jsonPath("$.amount", is(order.getAmount())))
                .andExpect(jsonPath("$.status", is(order.getStatus().getStatus())))
                .andExpect(jsonPath("$.importHash", is(order.getImportHash())))
                .andExpect(jsonPath("$.product.id", is(order.getProduct().getId().toString())))
                .andExpect(jsonPath("$.product.title", is(order.getProduct().getTitle())))
                .andExpect(jsonPath("$.user.id", is(order.getUser().getId().toString())))
                .andExpect(jsonPath("$.user.email", is(order.getUser().getEmail())));

        verify(orderService).updateOrder(any(UUID.class), any(OrderRequest.class));
    }

    @Test
    void testDeleteOrder() throws Exception {
        willDoNothing().given(orderService).deleteOrder(any(UUID.class));

        mockMvc.perform(delete("/orders/{id}", UUID.randomUUID())
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(orderService).deleteOrder(any(UUID.class));
    }

}
