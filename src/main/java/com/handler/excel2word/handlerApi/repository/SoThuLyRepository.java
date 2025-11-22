package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.entity.SoThuLyKiemSoat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SoThuLyRepository extends JpaRepository<SoThuLyKiemSoat, Long> {
    @Query("SELECT s FROM SoThuLyKiemSoat s " +
            "WHERE (:from IS NULL OR s.createdAt >= :from) " +
            "AND (:to IS NULL OR s.createdAt <= :to)")
    Page<SoThuLyKiemSoat> searchByCreateAt(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable);

    @Query(" SELECT s FROM SoThuLyKiemSoat s WHERE (:from IS NULL OR s.createdAt >= :from) " +
            "AND (:to IS NULL OR s.createdAt <= :to) " +
            "ORDER BY s.orderNumber ASC, s.id DESC")
    List<SoThuLyKiemSoat> findAllByFilter(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}
