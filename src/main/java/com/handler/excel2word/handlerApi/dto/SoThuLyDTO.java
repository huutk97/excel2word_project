package com.handler.excel2word.handlerApi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SoThuLyDTO {

    private Long id;
    private String sttNgayTl;
    private String banAnQuyetDinh;
    private String personWhoMustExecute;
    private String personToBeExecuted;

    private String qdUyThacDi;
    private String qdUyThacDen;

    private String qdTha;
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
}
