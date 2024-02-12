package com.flatlogic.app.ecomerce.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditable extends AbstractIdentifiable<UUID> {

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`createdById`", nullable = false, updatable = false)
    private User createdBy;

    @CreatedDate
    @Column(name = "`createdAt`", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`updatedById`", insertable = false)
    private User updatedBy;

    @LastModifiedDate
    @Column(name = "`updatedAt`", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "`deletedAt`", insertable = false)
    private LocalDateTime deletedAt;

}
