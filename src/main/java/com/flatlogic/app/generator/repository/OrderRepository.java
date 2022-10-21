package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("select o from Order o where o.deletedAt is null")
    List<Order> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("select o from Order o where o.deletedAt is null")
    List<Order> findAllByDeletedAtIsNull(Sort sort);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.product.id = null where o.product.id = :productId")
    void updateProductIdAtNull(@Param(value = "productId") UUID productId);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.user.id = null where o.user.id = :userId")
    void updateUserIdAtNull(@Param(value = "userId") UUID userId);

    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.deletedAt = :deletedAt where o.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") LocalDateTime deletedAt);

}
