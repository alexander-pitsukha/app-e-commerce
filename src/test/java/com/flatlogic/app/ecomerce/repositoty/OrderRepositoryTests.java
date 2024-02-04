package com.flatlogic.app.ecomerce.repositoty;

import com.flatlogic.app.ecomerce.entity.Order;
import com.flatlogic.app.ecomerce.repository.OrderRepository;
import com.flatlogic.app.ecomerce.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllByDeletedAtIsNullPage() {
        List<Order> orders = orderRepository.findAllByDeletedAtIsNull(Pageable.ofSize(10));

        assertEquals(2, orders.size());
    }

    @Test
    void testFindAllByDeletedAtIsNullSort() {
        List<Order> orders = orderRepository.findAllByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());

        assertEquals(2, orders.size());
    }

    @Test
    void testUpdateProductIdAtNull() {
        orderRepository.updateProductIdAtNull(UUID.fromString("0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4"));
        Order order = entityManager.find(Order.class, UUID.fromString("8050f33a-1903-4ac7-a7d1-a6c3a77834bd"));

        assertNull(order.getProduct());
    }

    @Test
    void testUpdateUserIdAtNull() {
        orderRepository.updateUserIdAtNull(UUID.fromString("127b7b84-5af0-4e66-93ca-346ac35e1bdd"));
        Order order = entityManager.find(Order.class, UUID.fromString("8050f33a-1903-4ac7-a7d1-a6c3a77834bd"));

        assertNull(order.getUser());
    }

    @Test
    void testUpdateDeletedAt() {
        UUID id = UUID.fromString("8050f33a-1903-4ac7-a7d1-a6c3a77834bd");

        orderRepository.updateDeletedAt(id, LocalDateTime.now());
        Order order = entityManager.find(Order.class, id);

        assertNotNull(order.getDeletedAt());
    }

}
