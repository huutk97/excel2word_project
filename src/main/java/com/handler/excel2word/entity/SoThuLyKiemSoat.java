package com.handler.excel2word.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "so_thu_ly_kiem_soat")
@Getter
@Setter
public class SoThuLyKiemSoat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "stt_ngay_tl")
    private String sttNgayTl;

    @Column(name = "ban_an_quyet_dinh")
    private String banAnQuyetDinh;

    @Column(name = "person_who_must_execute")
    private String personWhoMustExecute;

    @Column(name = "person_to_be_executed")
    private String personToBeExecuted;

    @Column(name = "qd_uy_thac_di")
    private String qdUyThacDi;

    @Column(name = "qd_uy_thac_den")
    private String qdUyThacDen;

    @Column(name = "qd_tha")
    private String qdTha;

    @Column(name = "nd_thi_hanh")
    private String ndThiHanh; // Nội dung thi hành ( Các khoản phải thi hành, số tiền)

    @Column(name = "qd_chua_co_dieu_kien")
    private String qdChuaCoDieuKien;


    @Column(name = "qd_rut_tha")
    private String qdRutTha;

    @Column(name = "qd_hoan_tha")
    private String qdHoanTha;

    @Column(name = "qd_tiep_tuc_sau_hoan")
    private String qdTiepTucSauHoan;

    @Column(name = "qd_tam_dinh_chi")
    private String qdTamDinhChi;

    @Column(name = "qd_tiep_tuc_sau_tam_dinh_chi")
    private String qdTiepTucSauTamDinhChi;

    @Column(name = "qd_dinh_chi")
    private String qdDinhChi;

    @Column(name = "da_thi_hanh_xong")
    private String daThiHanhXong;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "ve_thoi_han_gui_qd")
    private String veThoiHanGuiQD;
    @Column(name = "ve_can_cu_ban_hanh_qd")
    private String veCanCuBanHanhQD;
    @Column(name = "ve_tham_quyen_ban_hanh_qd")
    private String veThamQuyenBanHanhQD;
    @Column(name = "ve_hinh_thuc_qd")
    private String veHinhThucQD;
    @Column(name = "ve_noi_dung_qd")
    private String veNoiDungQD;
    @Column(name = "noi_dung_khac")
    private String noiDungKhac;
    @Column(name = "quan_diem_ksv")
    private String quanDiemKSV;

    @Transient
    private String formatDateNgayTl;

    public String getFormatDateNgayTl() {
        if (this.sttNgayTl == null) {
            return "";
        }
        try {
            String[] parts = this.sttNgayTl.split("-");
            String day = parts[0].trim();
            String month = parts[1].trim();
            String year = parts[2].trim();

            return String.format("%s - %s/%s/%s", this.orderNumber, day, month, year);

        } catch (Exception e) {
            return this.sttNgayTl; // fallback
        }
    }

}