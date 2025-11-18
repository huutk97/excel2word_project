package com.handler.excel2word.handlerApi.service;

import com.handler.excel2word.dto.SoThuLyKiemSoatDTO;
import com.handler.excel2word.entity.SoThuLyKiemSoat;
import com.handler.excel2word.handlerApi.Interface.SoThuLyService;
import com.handler.excel2word.handlerApi.dto.SoThuLyDTO;
import com.handler.excel2word.handlerApi.repository.SoThuLyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
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
        LocalDate fromDate = dto.getBeginDate() == null ? null : dto.getBeginDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate toDate = dto.getEndDate() == null ? null : dto.getEndDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Page<SoThuLyKiemSoat> soThuLyKiemSoats = repository.searchByCreateAt(fromDate, toDate, pageable);
        int sizePage = Objects.nonNull(soThuLyKiemSoats) && !CollectionUtils.isEmpty(soThuLyKiemSoats.getContent()) ? soThuLyKiemSoats.getContent().size() : 0;
        log.info("SoThuLyKiemSoat page: {}", sizePage);
        return soThuLyKiemSoats;
    }

    @Override
    public SoThuLyKiemSoat getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public SoThuLyDTO findById(Long id) {
        SoThuLyKiemSoat entity = repository.findById(id).orElse(null);
        SoThuLyDTO dto = convertEntityToDTO(entity);
        return dto;
    }

    @Override
    public List<SoThuLyKiemSoat> getAll() {
        return repository.findAll();
    }

    @Override
    public List<SoThuLyKiemSoatDTO> exportExcel(SoThuLyDTO dto) {
        List<SoThuLyKiemSoat> list =  repository.findAll();
        return convertEntityToDTOs(list);
    }


    // ---------- MAPPING ----------
    private SoThuLyKiemSoat map(SoThuLyDTO dto) {
        SoThuLyKiemSoat e = new SoThuLyKiemSoat();
        updateEntity(e, dto);
        return e;
    }

    private List<SoThuLyKiemSoatDTO> convertEntityToDTOs(List<SoThuLyKiemSoat> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        List<SoThuLyKiemSoatDTO> result = new ArrayList<>();
        for (SoThuLyKiemSoat e : entities) {
            SoThuLyKiemSoatDTO dto = convertEntityToExcelDTO(e);
            result.add(dto);
        }
        return result;
    }

    private SoThuLyDTO convertEntityToDTO(SoThuLyKiemSoat e) {
        if (e == null) {
            return new SoThuLyDTO();
        }
        SoThuLyDTO dto = new SoThuLyDTO();
        dto.setOrderNumber(e.getOrderNumber());

        String input = e.getSttNgayTl();
        String[] parts = input.split("-");
        if (parts.length > 2) {
            dto.setNgayTL(parts[0].trim());
            dto.setThangTL(parts[1].trim());
            dto.setNamTL(parts[2].trim());
        }
        dto.setSttNgayTl(e.getSttNgayTl());
        dto.setBanAnQuyetDinh(e.getBanAnQuyetDinh());
        dto.setPersonWhoMustExecute(e.getPersonWhoMustExecute());
        dto.setPersonToBeExecuted(e.getPersonToBeExecuted());

        dto.setQdUyThacDi(e.getQdUyThacDi());
        dto.setQdUyThacDen(e.getQdUyThacDen());

        dto.setQdTha(e.getQdTha());
        dto.setNdThiHanh(e.getNdThiHanh());
        dto.setQdChuaCoDieuKien(e.getQdChuaCoDieuKien());
        dto.setQdRutTha(e.getQdRutTha());

        dto.setQdHoanTha(e.getQdHoanTha());
        dto.setQdTiepTucSauHoan(e.getQdTiepTucSauHoan());

        dto.setQdTamDinhChi(e.getQdTamDinhChi());
        dto.setQdTiepTucSauTamDinhChi(e.getQdTiepTucSauTamDinhChi());

        dto.setQdDinhChi(e.getQdDinhChi());
        dto.setDaThiHanhXong(e.getDaThiHanhXong());

        dto.setGhiChu(e.getGhiChu());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());

        dto.setVeThoiHanGuiQD(e.getVeThoiHanGuiQD());
        dto.setVeCanCuBanHanhQD(e.getVeCanCuBanHanhQD());
        dto.setVeThamQuyenBanHanhQD(e.getVeThamQuyenBanHanhQD());
        dto.setVeHinhThucQD(e.getVeHinhThucQD());
        dto.setVeNoiDungQD(e.getVeNoiDungQD());
        dto.setNoiDungKhac(e.getNoiDungKhac());
        dto.setQuanDiemKSV(e.getQuanDiemKSV());
        return dto;
    }

    private SoThuLyKiemSoatDTO convertEntityToExcelDTO(SoThuLyKiemSoat e) {
        if (e == null) {
            return new SoThuLyKiemSoatDTO();
        }
        SoThuLyKiemSoatDTO dto = new SoThuLyKiemSoatDTO();
        dto.setOrderNumber(e.getOrderNumber());
        dto.setSttNgayTl(e.getSttNgayTl());
        dto.setBanAnQuyetDinh(e.getBanAnQuyetDinh());
        dto.setPersonWhoMustExecute(e.getPersonWhoMustExecute());
        dto.setPersonToBeExecuted(e.getPersonToBeExecuted());

        dto.setQuyetDinhUyThacDi(e.getQdUyThacDi());
        dto.setQuyetDinhUyThacDen(e.getQdUyThacDen());

        dto.setQuyetDinhThiHanhAnDanSu(e.getQdTha());
        dto.setNdThiHanh(e.getNdThiHanh());
        dto.setQuyetDinhChuaCoDieuKien(e.getQdChuaCoDieuKien());
        dto.setQuyetDinhRutQuyetDinhTHA(e.getQdRutTha());

        dto.setQuyetDinhHoanTHA(e.getQdHoanTha());
        dto.setQuyetDinhTiepTucTHA(e.getQdTiepTucSauHoan());

        dto.setQuyetDinhTamDinhChi(e.getQdTamDinhChi());
        dto.setQuyetDinhTiepTucTHATamDinhChi(e.getQdTiepTucSauTamDinhChi());

        dto.setQuyetDinhDinhChiTHA(e.getQdDinhChi());
        dto.setDaThiHanhXong(e.getDaThiHanhXong());

        dto.setGhiChu(e.getGhiChu());
        dto.setOrderNumber(e.getOrderNumber());

        dto.setVeThoiHanGuiQD(e.getVeThoiHanGuiQD());
        dto.setVeCanCuBanHanhQD(e.getVeCanCuBanHanhQD());
        dto.setVeThamQuyenBanHanhQD(e.getVeThamQuyenBanHanhQD());
        dto.setVeHinhThucQD(e.getVeHinhThucQD());
        dto.setVeNoiDungQD(e.getVeNoiDungQD());
        dto.setNoiDungKhac(e.getNoiDungKhac());
        dto.setQuanDiemKSV(e.getQuanDiemKSV());
        return dto;
    }

    private void updateEntity(SoThuLyKiemSoat e, SoThuLyDTO dto) {
        e.setOrderNumber(dto.getOrderNumber());
        e.setSttNgayTl(dto.getSttNgayTl());
        e.setBanAnQuyetDinh(dto.getBanAnQuyetDinh());
        e.setPersonWhoMustExecute(dto.getPersonWhoMustExecute());
        e.setPersonToBeExecuted(dto.getPersonToBeExecuted());

        e.setQdUyThacDi(dto.getQdUyThacDi());
        e.setQdUyThacDen(dto.getQdUyThacDen());

        e.setQdTha(dto.getQdTha());
        e.setNdThiHanh(dto.getNdThiHanh());
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

        e.setVeThoiHanGuiQD(dto.getVeThoiHanGuiQD());
        e.setVeCanCuBanHanhQD(dto.getVeCanCuBanHanhQD());
        e.setVeThamQuyenBanHanhQD(dto.getVeThamQuyenBanHanhQD());
        e.setVeHinhThucQD(dto.getVeHinhThucQD());
        e.setVeNoiDungQD(dto.getVeNoiDungQD());
        e.setNoiDungKhac(dto.getNoiDungKhac());
        e.setQuanDiemKSV(dto.getQuanDiemKSV());
    }
}
