package com.flatlogic.app.ecomerce.service.impl;

import com.flatlogic.app.ecomerce.controller.request.OrderRequest;
import com.flatlogic.app.ecomerce.entity.Order;
import com.flatlogic.app.ecomerce.exception.NoSuchEntityException;
import com.flatlogic.app.ecomerce.repository.OrderRepository;
import com.flatlogic.app.ecomerce.repository.ProductRepository;
import com.flatlogic.app.ecomerce.repository.UserRepository;
import com.flatlogic.app.ecomerce.service.OrderService;
import com.flatlogic.app.ecomerce.type.OrderStatusType;
import com.flatlogic.app.ecomerce.type.OrderType;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * OrderService service.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MessageCodeUtil messageCodeUtil;

    /**
     * Get orders.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Orders
     */
    @Override
    public List<Order> getOrders(final Integer offset, final Integer limit, final String orderBy) {
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
            return orderRepository.findAllByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return orderRepository.findAllByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get order by id.
     *
     * @param id Order Id
     * @return Order
     */
    @Override
    public Order getOrderById(final UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * Save order.
     *
     * @param orderRequest Order data
     * @return Order
     */
    @Override
    @Transactional
    public Order saveOrder(final OrderRequest orderRequest) {
        var order = new Order();
        setFieldsData(orderRequest, order);
        return orderRepository.save(order);
    }

    /**
     * Update order.
     *
     * @param id           Order Id
     * @param orderRequest Order data
     * @return Order
     */
    @Override
    @Transactional
    public Order updateOrder(final UUID id, final OrderRequest orderRequest) {
        var order = Optional.ofNullable(getOrderById(id)).orElseThrow(() -> new NoSuchEntityException(
                messageCodeUtil.getFullErrorMessageByBundleCode(Constants.ERROR_MSG_ORDER_BY_ID_NOT_FOUND,
                        new Object[]{id})));
        setFieldsData(orderRequest, order);
        return order;
    }

    /**
     * Delete order.
     *
     * @param id Order Id
     */
    @Override
    @Transactional
    public void deleteOrder(final UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_ORDER_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        orderRepository.updateDeletedAt(id, LocalDateTime.now());
    }

    private void setFieldsData(final OrderRequest orderRequest, final Order order) {
        order.setOrderDate(orderRequest.getOrderDate());
        order.setAmount(orderRequest.getAmount());
        order.setStatus(OrderStatusType.valueOfStatus(orderRequest.getStatus()));
        order.setImportHash(orderRequest.getImportHash());
        Optional.ofNullable(orderRequest.getProductId()).ifPresent(productId ->
                order.setProduct(productRepository.getById(productId)));
        Optional.ofNullable(orderRequest.getUserId()).ifPresent(userId ->
                order.setUser(userRepository.getById(userId)));
    }

}
