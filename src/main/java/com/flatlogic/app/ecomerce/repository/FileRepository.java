package com.flatlogic.app.ecomerce.repository;

import com.flatlogic.app.ecomerce.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    @Query("select case when count(f) > 0 then true else false end from File f where exists (select file from File file where file.privateUrl = :privateUrl)")
    boolean existsByPrivateUrl(String privateUrl);

    @Query("select f from File f where f.belongsTo = :belongsTo")
    List<File> findAllByBelongsTo(@Param(value = "belongsTo") String belongsTo);

}
