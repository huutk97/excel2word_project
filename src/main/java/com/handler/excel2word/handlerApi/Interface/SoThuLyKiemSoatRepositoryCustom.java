package com.handler.excel2word.handlerApi.Interface;

import com.handler.excel2word.handlerApi.entity.SoThuLyKiemSoat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface SoThuLyKiemSoatRepositoryCustom {
    List<SoThuLyKiemSoat> findByDateRange(Date from, Date to, Long userId, String account);

    Page<SoThuLyKiemSoat> findByDateRange(Date from, Date to, Long userId, String account, Pageable pageable);
}
