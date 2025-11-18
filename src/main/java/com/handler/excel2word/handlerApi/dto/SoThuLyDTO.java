package com.handler.excel2word.handlerApi.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SoThuLyDTO {

    private Long id;
    private String orderNumber;
    private String sttNgayTl;

    private String ngayTL;
    private String thangTL;
    private String namTL;

    private String banAnQuyetDinh;
    private String personWhoMustExecute;
    private String personToBeExecuted;

    private String qdUyThacDi;
    private String quyetDinhTha;
    private String qdUyThacDen;

    private String qdTha;
    private String ndThiHanh;
    private String qdChuaCoDieuKien;
    private String qdRutTha;

    private String qdHoanTha;
    private String qdTiepTucSauHoan;

    private String qdTamDinhChi;
    private String qdTiepTucSauTamDinhChi;

    private String qdDinhChi;
    private String daThiHanhXong;

    private String ghiChu;
    private Date createAt;
    private Date updatedAt;
    private Date beginDate;
    private Date endDate;
    private Date createdAt;

    private String veThoiHanGuiQD;
    private String veCanCuBanHanhQD;
    private String veThamQuyenBanHanhQD;
    private String veHinhThucQD;
    private String veNoiDungQD;
    private String noiDungKhac;
    private String quanDiemKSV;

    private String formatDateNgayTl;
}
