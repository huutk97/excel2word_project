package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.handlerApi.entity.SoThuLyKiemSoat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SoThuLyRepository extends JpaRepository<SoThuLyKiemSoat, Long> {

    @Query("SELECT s FROM SoThuLyKiemSoat s WHERE TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') BETWEEN :from AND :to")
    Page<SoThuLyKiemSoat> findByRange(@Param("from") Date from, @Param("to") Date to, Pageable pageable);

    @Query("SELECT s FROM SoThuLyKiemSoat s WHERE TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') >= :from")
    Page<SoThuLyKiemSoat> findFrom(@Param("from") Date from, Pageable pageable);

    @Query("SELECT s FROM SoThuLyKiemSoat s WHERE TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') <= :to")
    Page<SoThuLyKiemSoat> findTo(@Param("to") Date to, Pageable pageable);

    @Query("SELECT s FROM SoThuLyKiemSoat s")
    Page<SoThuLyKiemSoat> findAllData(Pageable pageable);

    // export excel
    @Query("SELECT s FROM SoThuLyKiemSoat s " +
            "WHERE TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') BETWEEN :from AND :to " +
            "ORDER BY s.orderNumber ASC, s.id DESC")
    List<SoThuLyKiemSoat> findRange(@Param("from") Date from, @Param("to") Date to);

    @Query("SELECT s FROM SoThuLyKiemSoat s " +
            "WHERE TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') >= :from " +
            "ORDER BY s.orderNumber ASC, s.id DESC")
    List<SoThuLyKiemSoat> findFrom(@Param("from") Date from);

    @Query("SELECT s FROM SoThuLyKiemSoat s " +
            "WHERE TO_DATE(s.sttNgayTl, 'DD-MM-YYYY') <= :to " +
            "ORDER BY s.orderNumber ASC, s.id DESC")
    List<SoThuLyKiemSoat> findTo(@Param("to") Date to);

    @Query("SELECT s FROM SoThuLyKiemSoat s ORDER BY s.orderNumber ASC, s.id DESC")
    List<SoThuLyKiemSoat> findAllOrder();
}
