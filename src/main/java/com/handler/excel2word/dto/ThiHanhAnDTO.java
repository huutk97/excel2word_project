package com.handler.excel2word.dto;
import com.handler.excel2word.core.export.ExcelColumn;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ThiHanhAnDTO {

    @ExcelColumn(name = "STT", index = 1)
    private int stt;

    @ExcelColumn(name = "Ngày TL", index = 2)
    private String sttNgayTl;

    @ExcelColumn(name = "Bản án, Quyết định (Số; Ngày, tháng, năm; Cơ quan ban hành)", index = 3)
    private String banAnQuyetDinh;

    @ExcelColumn(name = "Người phải thi hành (tên địa chỉ)", index = 4)
    private String personWhoMustExecute;

    @ExcelColumn(name = "Người được thi hành (tên địa chỉ)", index = 5)
    private String personToBeExecuted;

    @ExcelColumn(name = "QĐ Ủy thác đi (Số; Ngày, tháng, năm; Số tiền; Nơi BH)", index = 6)
    private String quyetDinhUyThacDi;

    @ExcelColumn(name = "QĐ Ủy thác đến (Số; Ngày, tháng, năm; Số tiền; Nơi BH)", index = 7)
    private String quyetDinhUyThacDen;

    @ExcelColumn(name = "QĐ thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)", index = 8)
    private String quyetDinhThiHanhAnDanSu;

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
}

