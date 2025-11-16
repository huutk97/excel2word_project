package com.handler.excel2word.handlerApi.Interface;

import com.handler.excel2word.dto.ThiHanhAnDTO;
import com.handler.excel2word.entity.SoThuLyKiemSoat;
import com.handler.excel2word.handlerApi.dto.SoThuLyDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SoThuLyService {

    SoThuLyKiemSoat create(SoThuLyDTO dto);

    SoThuLyKiemSoat update(Long id, SoThuLyDTO dto);

    void delete(Long id);

    SoThuLyKiemSoat getById(Long id);

    List<SoThuLyKiemSoat> getAll();

    Page<SoThuLyKiemSoat> queryPage(SoThuLyDTO dto, int page, int size);

    List<ThiHanhAnDTO> exportExcel(SoThuLyDTO dto) ;
}
