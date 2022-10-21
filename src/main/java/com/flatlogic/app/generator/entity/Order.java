package com.flatlogic.app.generator.entity;

import com.flatlogic.app.generator.type.OrderStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends AbstractEntity<UUID> {

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "amount")
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatusType status;

    @Column(name = "`importHash`", unique = true)
    private String importHash;

    @ManyToOne
    @JoinColumn(name = "`productId`", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "`userId`", nullable = false)
    private User user;

}
