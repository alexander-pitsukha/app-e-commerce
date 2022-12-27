package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.controller.request.RequestData;
import com.flatlogic.app.ecomerce.controller.request.GetModelAttribute;
import com.flatlogic.app.ecomerce.controller.request.OrderRequest;
import com.flatlogic.app.ecomerce.controller.data.RowsData;
import com.flatlogic.app.ecomerce.dto.OrderDto;
import com.flatlogic.app.ecomerce.entity.Order;
import com.flatlogic.app.ecomerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * OrderController REST controller.
 */
@Tag(name = "Order controller", description = "Order resources that provides access to available order data")
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final DefaultConversionService defaultConversionService;

    /**
     * Get orders.
     *
     * @param modelAttribute GetModelAttribute
     * @return Order RowsWrapper
     */
    @Operation(summary = "Get orders", description = "Provides all available order list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RowsData<OrderDto>",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping
    public ResponseEntity<RowsData<OrderDto>> getOrders(@ModelAttribute GetModelAttribute modelAttribute) {
        log.info("Get orders.");
        RowsData<OrderDto> rowsData = new RowsData<>();
        List<Order> orders = orderService.getOrders(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<OrderDto> orderDtos = orders.stream().map(order -> defaultConversionService
                .convert(order, OrderDto.class)).collect(Collectors.toList());
        rowsData.setRows(orderDtos);
        rowsData.setCount(orderDtos.size());
        return ResponseEntity.ok(rowsData);
    }

    /**
     * Get order by id.
     *
     * @param id Order Id
     * @return Order
     */
    @Operation(summary = "Get order by id", description = "Provides available order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return OrderDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        log.info("Get order by  id = {}.", id);
        return Optional.ofNullable(orderService.getOrderById(id))
                .map(order -> ResponseEntity.ok(defaultConversionService.convert(order, OrderDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save order.
     *
     * @param requestData RequestData
     * @return Order
     */
    @Operation(summary = "Save order", description = "Provides saving order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return OrderDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@RequestBody RequestData<OrderRequest> requestData) {
        log.info("Save order.");
        var order = orderService.saveOrder(requestData.getData());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                        .buildAndExpand(order.getId()).toUri())
                .body(defaultConversionService.convert(order, OrderDto.class));
    }

    /**
     * Update order.
     *
     * @param id          Order id
     * @param requestData RequestData
     * @return Order
     */
    @Operation(summary = "Update order", description = "Provides updating order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return OrderDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable UUID id,
                                                @RequestBody RequestData<OrderRequest> requestData) {
        log.info("Update order id = {}.", id);
        var order = orderService.updateOrder(id, requestData.getData());
        return ResponseEntity.ok(defaultConversionService.convert(order, OrderDto.class));
    }

    /**
     * Delete order.
     *
     * @param id Order id
     * @return Void
     */
    @Operation(summary = "Delete order", description = "Provides deleting order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable UUID id) {
        log.info("Delete order id = {}.", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
