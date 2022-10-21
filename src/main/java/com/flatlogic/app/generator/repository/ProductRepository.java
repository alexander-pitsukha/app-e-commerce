package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("select p from Product p where p.deletedAt is null")
    List<Product> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("select p from Product p where p.deletedAt is null")
    List<Product> findAllByDeletedAtIsNull(Sort sort);

    @Query("select p from Product p where p.title like ?1% and p.deletedAt is null")
    List<Product> findAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(String query, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Product p set p.deletedAt = :deletedAt where p.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") LocalDateTime deletedAt);

}
