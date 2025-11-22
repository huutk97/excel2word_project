package com.handler.excel2word.handlerApi.Interface;

import com.handler.excel2word.dto.SoThuLyKiemSoatDTO;
import com.handler.excel2word.entity.SoThuLyKiemSoat;
import com.handler.excel2word.handlerApi.dto.SoThuLyDTO;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface SoThuLyService {

    SoThuLyKiemSoat create(SoThuLyDTO dto);

    SoThuLyKiemSoat update(Long id, SoThuLyDTO dto);

    void delete(Long id);

    SoThuLyKiemSoat getById(Long id);

    SoThuLyDTO findById(Long id);

    List<SoThuLyKiemSoat> getAll();

    Page<SoThuLyKiemSoat> queryPage(SoThuLyDTO dto, int page, int size);

    void deleteAllById(List<Long> ids);

    List<SoThuLyKiemSoatDTO> exportExcel(SoThuLyDTO dto) ;

    SoThuLyDTO copyFromId(SoThuLyKiemSoat existing) throws InvocationTargetException, IllegalAccessException;
}
