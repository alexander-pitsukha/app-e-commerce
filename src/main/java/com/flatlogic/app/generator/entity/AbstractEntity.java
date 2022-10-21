package com.flatlogic.app.generator.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<I extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private I id;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "`createdById`", updatable = false)
    private User createdBy;

    @CreatedDate
    @Column(name = "`createdAt`", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "`updatedById`", insertable = false)
    private User updatedBy;

    @LastModifiedDate
    @Column(name = "`updatedAt`", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "`deletedAt`", insertable = false)
    private LocalDateTime deletedAt;

}
