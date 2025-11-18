package com.handler.excel2word.dto;
import com.handler.excel2word.core.export.ExcelColumn;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SoThuLyKiemSoatDTO {

//    @ExcelColumn(name = "STT", index = 1)
    private int stt;

    @ExcelColumn(name = "Ngày TL", index = 1, textMethod = "formatDateNgayTl")
    private String sttNgayTl;

    @ExcelColumn(name = "Bản án, Quyết định (Số; Ngày, tháng, năm; Cơ quan ban hành)", index = 2)
    private String banAnQuyetDinh;

    @ExcelColumn(name = "Người phải thi hành (tên địa chỉ)", index = 3)
    private String personWhoMustExecute;

    @ExcelColumn(name = "Người được thi hành (tên địa chỉ)", index = 4)
    private String personToBeExecuted;

    @ExcelColumn(name = "QĐ Ủy thác đi (Số; Ngày, tháng, năm; Số tiền; Nơi BH)", index = 5)
    private String quyetDinhUyThacDi;

    @ExcelColumn(name = "QĐ Ủy thác đến (Số; Ngày, tháng, năm; Số tiền; Nơi BH)", index = 6)
    private String quyetDinhUyThacDen;

    @ExcelColumn(name = "QĐ thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)", index = 7)
    private String quyetDinhThiHanhAnDanSu;

    @ExcelColumn(name = "Nội dung thi hành ( Các khoản phải thi hành, số tiền)", index = 8)
    private String ndThiHanh;

    @ExcelColumn(name = "QĐ về việc chưa có điều kiện thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)", index = 9)
    private String quyetDinhChuaCoDieuKien;

    @ExcelColumn(name = "QĐ rút Quyết định THA (Số; Ngày, tháng, năm; Số tiền)", index = 10)
    private String quyetDinhRutQuyetDinhTHA;

    @ExcelColumn(name = "QĐ hoãn thi hành án Dân sự (Số; Ngày, tháng, năm; Lý do; Số tiền)", index = 11)
    private String quyetDinhHoanTHA;

    @ExcelColumn(name = "QĐ tiếp tục THA (Số; Ngày, tháng, năm)", index = 12)
    private String quyetDinhTiepTucTHA;

    @ExcelColumn(name = "QĐ tạm đình chỉ thi hành án dân sự (Số; Ngày, tháng, năm; Lý do; Số tiền)", index = 13)
    private String quyetDinhTamDinhChi;

    @ExcelColumn(name = "QĐ tiếp tục THA (Số; Ngày, tháng, năm)", index = 14)
    private String quyetDinhTiepTucTHATamDinhChi;

    @ExcelColumn(name = "QĐ đình chỉ thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)", index = 15)
    private String quyetDinhDinhChiTHA;

    @ExcelColumn(name = "Đã thi hành xong (Số; Ngày, tháng, năm; Số tiền)", index = 16)
    private String daThiHanhXong;

    @ExcelColumn(name = "Ghi chú (Ghi các thông tin như tên chấp hành viên; Vi phạm..)", index = 17)
    private String ghiChu;

    @ExcelColumn(name = "Về thời hạn gửi Quyết định", index = 18)
    private String veThoiHanGuiQD;
    @ExcelColumn(name = "Về căn cứ ban hành Quyết định", index = 19)
    private String veCanCuBanHanhQD;
    @ExcelColumn(name = "Về thẩm quyền ban hành Quyết định", index = 20)
    private String veThamQuyenBanHanhQD;
    @ExcelColumn(name = "Về hình thức của Quyết định", index = 21)
    private String veHinhThucQD;
    @ExcelColumn(name = "Về nội dung của Quyết định", index = 22)
    private String veNoiDungQD;
    @ExcelColumn(name = "Những nội dung khác", index = 23)
    private String noiDungKhac;
    @ExcelColumn(name = "Quan điểm của KSV", index = 24)
    private String quanDiemKSV;

    private String orderNumber;

    public String formatDateNgayTl() {
        if (this.sttNgayTl == null) {
            return "";
        }
        String [] parts = this.sttNgayTl.split("-");
        return orderNumber + " - " + parts[0].trim() + "/" + parts[1].trim() + "/" + parts[2].trim();
    }
}

