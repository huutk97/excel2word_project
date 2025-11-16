package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.entity.SoThuLyKiemSoat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SoThuLyRepository extends JpaRepository<SoThuLyKiemSoat, Long> {
    @Query("SELECT s FROM SoThuLyKiemSoat s " +
            "WHERE (:from IS NULL OR s.createdAt >= :from) " +
            "AND (:to IS NULL OR s.createdAt <= :to)")
    Page<SoThuLyKiemSoat> searchByCreateAt(
            @Param("from") Date from,
            @Param("to") Date to,
            Pageable pageable);
}
