package com.handler.excel2word.handlerApi.service;

import com.handler.excel2word.dto.ThiHanhAnDTO;
import com.handler.excel2word.entity.SoThuLyKiemSoat;
import com.handler.excel2word.handlerApi.Interface.SoThuLyService;
import com.handler.excel2word.handlerApi.dto.SoThuLyDTO;
import com.handler.excel2word.handlerApi.repository.SoThuLyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoThuLyServiceImpl implements SoThuLyService {

    private final SoThuLyRepository repository;

    @Override
    public SoThuLyKiemSoat create(SoThuLyDTO dto) {
        SoThuLyKiemSoat entity = map(dto);
        return repository.save(entity);
    }

    @Override
    public SoThuLyKiemSoat update(Long id, SoThuLyDTO dto) {
        SoThuLyKiemSoat entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        updateEntity(entity, dto);

        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<SoThuLyKiemSoat> queryPage(SoThuLyDTO dto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repository.searchByCreateAt(dto.getBeginDate(), dto.getEndDate(), pageable);
    }

    @Override
    public SoThuLyKiemSoat getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public List<SoThuLyKiemSoat> getAll() {
        return repository.findAll();
    }

    @Override
    public List<ThiHanhAnDTO> exportExcel(SoThuLyDTO dto) {
        List<SoThuLyKiemSoat> list =  repository.findAll();
        return convertEntityToDTOs(list);
    }


    // ---------- MAPPING ----------
    private SoThuLyKiemSoat map(SoThuLyDTO dto) {
        SoThuLyKiemSoat e = new SoThuLyKiemSoat();
        updateEntity(e, dto);
        return e;
    }

    private List<ThiHanhAnDTO> convertEntityToDTOs(List<SoThuLyKiemSoat> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        List<ThiHanhAnDTO> result = new ArrayList<>();
        int index = 1;
        for (SoThuLyKiemSoat e : entities) {
            ThiHanhAnDTO dto = convertEntityToDTO(e);
            dto.setStt(index++);
            result.add(dto);
        }
        return result;
    }

    private ThiHanhAnDTO convertEntityToDTO(SoThuLyKiemSoat e) {
        ThiHanhAnDTO dto = new ThiHanhAnDTO();
        dto.setSttNgayTl(e.getSttNgayTl());
        dto.setBanAnQuyetDinh(e.getBanAnQuyetDinh());
        dto.setPersonWhoMustExecute(e.getPersonWhoMustExecute());
        dto.setPersonToBeExecuted(e.getPersonToBeExecuted());

        dto.setQuyetDinhUyThacDi(e.getQdUyThacDi());
        dto.setQuyetDinhUyThacDen(e.getQdUyThacDen());

        dto.setQuyetDinhThiHanhAnDanSu(e.getQdTha());
        dto.setQuyetDinhChuaCoDieuKien(e.getQdChuaCoDieuKien());
        dto.setQuyetDinhRutQuyetDinhTHA(e.getQdRutTha());

        dto.setQuyetDinhHoanTHA(e.getQdHoanTha());
        dto.setQuyetDinhTiepTucTHA(e.getQdTiepTucSauHoan());

        dto.setQuyetDinhTamDinhChi(e.getQdTamDinhChi());
        dto.setQuyetDinhTiepTucTHATamDinhChi(e.getQdTiepTucSauTamDinhChi());

        dto.setQuyetDinhDinhChiTHA(e.getQdDinhChi());
        dto.setDaThiHanhXong(e.getDaThiHanhXong());

        dto.setGhiChu(e.getGhiChu());
        return dto;
    }

    private void updateEntity(SoThuLyKiemSoat e, SoThuLyDTO dto) {
        e.setSttNgayTl(dto.getSttNgayTl());
        e.setBanAnQuyetDinh(dto.getBanAnQuyetDinh());
        e.setPersonWhoMustExecute(dto.getPersonWhoMustExecute());
        e.setPersonToBeExecuted(dto.getPersonToBeExecuted());

        e.setQdUyThacDi(dto.getQdUyThacDi());
        e.setQdUyThacDen(dto.getQdUyThacDen());

        e.setQdTha(dto.getQdTha());
        e.setQdChuaCoDieuKien(dto.getQdChuaCoDieuKien());
        e.setQdRutTha(dto.getQdRutTha());

        e.setQdHoanTha(dto.getQdHoanTha());
        e.setQdTiepTucSauHoan(dto.getQdTiepTucSauHoan());

        e.setQdTamDinhChi(dto.getQdTamDinhChi());
        e.setQdTiepTucSauTamDinhChi(dto.getQdTiepTucSauTamDinhChi());

        e.setQdDinhChi(dto.getQdDinhChi());
        e.setDaThiHanhXong(dto.getDaThiHanhXong());

        e.setGhiChu(dto.getGhiChu());
        e.setCreatedAt(dto.getCreateAt());
        e.setUpdatedAt(new Date());
        if (e.getId() == null) {
            e.setCreatedAt(new Date());
            e.setUpdatedAt(null);
        }
    }
}
