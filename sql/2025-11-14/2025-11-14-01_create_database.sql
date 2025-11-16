
create schema thi_hanh_an;

CREATE TABLE so_thu_ly_kiem_soat (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     stt_ngay_tl VARCHAR(255) NULL COMMENT 'B : STT; Ngày TL',
                                     ban_an_quyet_dinh VARCHAR(255) NULL COMMENT 'C : Bản án, Quyết định (Số; Ngày, tháng, năm; Cơ quan ban hành)',
                                     person_who_must_execute VARCHAR(255) NULL COMMENT 'Người phải thi hành (Tên; Địa chỉ)',
                                     person_to_be_executed VARCHAR(255) NULL COMMENT 'Người được thi hành (tên địa chỉ)',
                                     quyet_dinh_uy_thac_di VARCHAR(255) NULL COMMENT 'F : QĐ uỷ thác đi (Số; Ngày, tháng, năm; Số tiền; Nơi BH)',
                                     quyet_dinh_uy_thac_den VARCHAR(255) NULL COMMENT 'G : QĐ uỷ thác đến (Số; Ngày, tháng, năm; Số tiền; Nơi BH)',
                                     quyet_dinh_tha VARCHAR(255) NULL COMMENT 'H: Quyết định thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)',
                                     qd_chua_co_dieu_kien VARCHAR(255) NULL COMMENT 'I : QĐ về việc chưa có điều kiện thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)',
                                     qd_rut_quyet_dinh_tha VARCHAR(255) NULL COMMENT 'J : QĐ rút Quyết định THA (Số; Ngày, tháng, năm; Số tiền)',
                                     qd_hoan_tha VARCHAR(255) NULL COMMENT 'QĐ hoãn thi hành án Dân sự (K : +  Số; Ngày, tháng, năm; Lý do Số tiền)',
                                     qd_tiep_tuc_tha VARCHAR(255) NULL COMMENT 'QĐ hoãn thi hành án Dân sự (L : + QĐ tiếp tục THA   (Số; Ngày, tháng, năm))',
                                     qd_tam_dinh_chi VARCHAR(255) NULL COMMENT 'QĐ tạm đình chỉ thi hành án dân sự (M:  +  Số; Ngày, tháng, năm; Lý do Số tiền)',
                                     qd_tiep_tuc_tha VARCHAR(255) NULL COMMENT 'QĐ tạm đình chỉ thi hành án dân sự (N : + QĐ tiếp tục THA   (Số; Ngày, tháng, năm\))',
                                     qd_dinh_chi VARCHAR(255) NULL COMMENT 'O : QĐ đình chỉ thi hành án dân sự  (Số; Ngày, tháng, năm; Số tiền)	',
                                     da_thi_hanh_xong VARCHAR(255) NULL COMMENT 'P: Đã thi hành xong  (Số; Ngày, tháng, năm; Số tiền)',
                                     ghi_chu VARCHAR(255) NULL COMMENT 'Q: Ghi chú (Ghi các thông tin như tên chấp hành viên; Vi phạm..)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
