package com.example.excel2word.dto;
import com.example.excel2word.utils.ExcelMapping;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ThiHanhAnDTO {

    @ExcelMapping(colIndex = 1)
    private Integer stt; // B : STT; Ngày TL

    @ExcelMapping(colIndex = 2)
    private String banAnQuyetDinh; // C : Bản án, Quyết định (Số; Ngày, tháng, năm; Cơ quan ban hành)

    @ExcelMapping(colIndex = 3)
    private String nguoiPhaiThiHanh; // D : Người phải thi hành

    @ExcelMapping(colIndex = 4)
    private String nguoiDuocThiHanh; // E : Người được thi hành (Tên; Địa chỉ)

    @ExcelMapping(colIndex = 5)
    private String quyetDinhUyThacDi; // F : QĐ uỷ thác đi (Số; Ngày, tháng, năm; Số tiền; Nơi BH)

    @ExcelMapping(colIndex = 6)
    private String quyetDinhUyThacDen; // G : QĐ uỷ thác đến (Số; Ngày, tháng, năm; Số tiền; Nơi BH)

    @ExcelMapping(colIndex = 7)
    private String quyetDinhThiHanhAnDanSu; // H : Quyết định thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)

    @ExcelMapping(colIndex = 8)
    private String quyetDinhChuaCoDieuKien; // I : QĐ về việc chưa có điều kiện thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)

    @ExcelMapping(colIndex = 9)
    private String quyetDinhRutQuyetDinhTHA; // J : QĐ rút Quyết định THA

    @ExcelMapping(colIndex = 10)
    private String quyetDinhHoanTHA; // K : QĐ hoãn thi hành án Dân sự (+ Số; Ngày, tháng, năm; Lý do Số tiền)

    @ExcelMapping(colIndex = 11)
    private String quyetDinhTiepTucTHA; // L : QĐ tiếp tục THA

    @ExcelMapping(colIndex = 12)
    private String quyetDinhTamDinhChi; // M: QĐ tạm đình chỉ thi hành án dân sự (+ Số; Ngày, tháng, năm; Lý do Số tiền)

    @ExcelMapping(colIndex = 13)
    private String quyetDinhTiepTucTHATamDinhChi; // N : QĐ tiếp tục THA

    @ExcelMapping(colIndex = 14)
    private String quyetDinhDinhChiTHA; // O : QĐ đình chỉ thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)

    @ExcelMapping(colIndex = 15)
    private String daThiHanhXong; // P: Đã thi hành xong (Số; Ngày, tháng, năm; Số tiền)

    @ExcelMapping(colIndex = 16)
    private String ghiChu; // Q: Ghi chú (tên chấp hành viên; vi phạm..)
}
