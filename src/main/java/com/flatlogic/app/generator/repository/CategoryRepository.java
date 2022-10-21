package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.Category;
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
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("select c from Category c where c.deletedAt is null")
    List<Category> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("select c from Category c where c.deletedAt is null")
    List<Category> findAllByDeletedAtIsNull(Sort sort);

    @Query("select c from Category c where c.title like ?1% and c.deletedAt is null")
    List<Category> findAllByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(String query, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Category с set с.deletedAt = :deletedAt where с.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") LocalDateTime deletedAt);

}
