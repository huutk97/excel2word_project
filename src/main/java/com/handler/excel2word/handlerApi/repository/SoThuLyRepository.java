package com.handler.excel2word.handlerApi.repository;

import com.handler.excel2word.handlerApi.entity.SoThuLyKiemSoat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoThuLyRepository extends JpaRepository<SoThuLyKiemSoat, Long> {
}
