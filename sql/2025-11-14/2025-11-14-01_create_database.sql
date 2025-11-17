
CREATE DATABASE thi_hanh_an OWNER postgres;
-- auto-generated definition
create table so_thu_ly_kiem_soat
(
    id                           bigserial
        primary key,
    stt_ngay_tl                  varchar(255),
    ban_an_quyet_dinh            varchar(255),
    person_who_must_execute      varchar(255),
    person_to_be_executed        varchar(255),
    quyet_dinh_uy_thac_di        varchar(255),
    quyet_dinh_uy_thac_den       varchar(255),
    quyet_dinh_tha               varchar(255),
    qd_chua_co_dieu_kien         varchar(255),
    qd_rut_quyet_dinh_tha        varchar(255),
    qd_hoan_tha                  varchar(255),
    qd_tiep_tuc_sau_hoan         varchar(255),
    qd_tam_dinh_chi              varchar(255),
    qd_tiep_tuc_sau_tam_dinh_chi varchar(255),
    qd_dinh_chi                  varchar(255),
    da_thi_hanh_xong             varchar(255),
    ghi_chu                      varchar(255),
    created_at                   date         default CURRENT_TIMESTAMP,
    updated_at                   date,
    qd_rut_tha                   varchar(255) default NULL::character varying,
    qd_uy_thac_den               varchar(255) default NULL::character varying,
    qd_uy_thac_di                varchar(255) default NULL::character varying,
    qd_tha                       varchar(255) default NULL::character varying
);

comment on column so_thu_ly_kiem_soat.stt_ngay_tl is 'B : STT; Ngày TL';

comment on column so_thu_ly_kiem_soat.ban_an_quyet_dinh is 'C : Bản án, Quyết định (Số; Ngày, tháng, năm; Cơ quan ban hành)';

comment on column so_thu_ly_kiem_soat.person_who_must_execute is 'Người phải thi hành (Tên; Địa chỉ)';

comment on column so_thu_ly_kiem_soat.person_to_be_executed is 'Người được thi hành (tên địa chỉ)';

comment on column so_thu_ly_kiem_soat.quyet_dinh_uy_thac_di is 'F : QĐ ủy thác đi (Số; Ngày, tháng, năm; Số tiền; Nơi BH)';

comment on column so_thu_ly_kiem_soat.quyet_dinh_uy_thac_den is 'G : QĐ ủy thác đến (Số; Ngày, tháng, năm; Số tiền; Nơi BH)';

comment on column so_thu_ly_kiem_soat.quyet_dinh_tha is 'H : Quyết định thi hành án dân sự (Số; Ngày, tháng, năm; Số tiền)';

comment on column so_thu_ly_kiem_soat.qd_chua_co_dieu_kien is 'I : QĐ chưa có điều kiện thi hành án';

comment on column so_thu_ly_kiem_soat.qd_rut_quyet_dinh_tha is 'J : QĐ rút THA';

comment on column so_thu_ly_kiem_soat.qd_hoan_tha is 'K : QĐ hoãn thi hành án Dân sự';

comment on column so_thu_ly_kiem_soat.qd_tiep_tuc_sau_hoan is 'L : QĐ tiếp tục THA sau hoãn';

comment on column so_thu_ly_kiem_soat.qd_tam_dinh_chi is 'M : QĐ tạm đình chỉ thi hành án dân sự';

comment on column so_thu_ly_kiem_soat.qd_tiep_tuc_sau_tam_dinh_chi is 'N : QĐ tiếp tục THA sau tạm đình chỉ';

comment on column so_thu_ly_kiem_soat.qd_dinh_chi is 'O : QĐ đình chỉ thi hành án dân sự';

comment on column so_thu_ly_kiem_soat.da_thi_hanh_xong is 'P : Đã thi hành xong';

comment on column so_thu_ly_kiem_soat.ghi_chu is 'Q : Ghi chú';


truncate table so_thu_ly_kiem_soat;
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (14, '14-10-2025', 'QĐ số: 72/2025/QĐST-HNGĐ ngày 28/08/2025 - TAND KV7 - NA ', 'Ông Ngô Xuân Trầm - Đ/c: Tân phú - NA', 'THA DS tỉnh Nghệ An', null, null, null, '', null, '', '', '', '', '', '', '', '2025-11-16', null, '', '', '', 'Số: 46/QĐ-THADS ngày 05/10/2025 APDSST: 150.000đ khấu trừ 300.000đ tạm ứng hoàn trả lại số tiền 150,000đ');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (13, '14-10-2025', 'Bản án số: 07/2025/DS-ST ngày 15/08/2025 - TAND KV7 - NA', 'Bà Đinh Thị tâm - Đ/c: xã Bạch Ngọc - NA', 'THA DS tỉnh Nghệ An', null, null, null, '', null, '', '', '', '', '', '', '', '2025-11-16', null, '', '', '', 'Số: 45/QĐ-THADS ngày 05/10/2025 số tiền APDSST 300,000đ khấu trừ tạm ứng đã nộp');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (12, '14-10-2025', 'QĐ số: 89/2025/QĐST-HNGĐ ngày 12/09/2025 - TAND KV7 - NA ', 'ông Đậu Bá Đức - Đ/c: Xóm Đông Sơn 5, xã Lương Sơn - NA', 'THA DS tỉnh Nghệ An', null, null, null, '', null, '', '', '', '', '', '', '', '2025-11-16', null, '', '', '', 'Số: 43/QĐ-THADS ngày 05/10/2025: APDSST: 150.000đ khấu trừ 300.000đ tạm ứng hoàn trả lại số tiền 150,000đ');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (11, '14-10-2025', 'QĐCN số:  06/2023/QĐCNHG-DS ngày 03/03/2025 TAND KV7 - NA', 'Ông Nguyễn Anh Tuấn - Đ/c: xã tân Kỳ - NA', 'Bà Nguyễn Thị Hồng - Đ/c: xã Tân Kỳ - NA', null, null, null, '', null, '', '', '', '', '', '', '', '2025-11-16', null, '', '', '', 'Số: 247/QĐ-THADS ngày 11/10/2025 trả lại diện tích đất: 357,3m2');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (10, '14-10-2025', 'QĐ số: 52/2015/QĐST-DS ngày 28/04/2020 - TAND KV7 - NA ', 'Ông Nguyễn Xuân tân - Đ/c: Xã Tân Phú - NA', 'Bà Nguyễn Thị Quy - Đ/c: Xã Tân Phú - NA', null, null, null, '', null, '', '', '', '', '', '', '', '2025-11-16', null, '', '', '', 'Số: 244/QĐ-THADS ngày 11/10/2025 trả số tiền cấp dưỡng nuôi con: 1.000.000đ/01 tháng (10/2025-09/2026)');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (9, '14-10-2025', 'QĐ số: 108/2015/DSST-HNGĐ ngày 30/09/2015 - TAND KV7 - NA ', 'Ông Lê Xuân Hải - Đ/c: Xóm Thanh Bình, Xã Tân An - NA', 'Bà Nguyễn Thị Quy - Đ/c: Xã Vĩnh Thành, xã  Tân Phú - NA', null, null, null, '', null, '', '', '', '', '', '', '', '2025-11-16', null, '', '', '', 'Số: 243/QĐ-THADS ngày 11/10/2025 trả số tiền cấp dưỡng nuôi con: 1.000.000đ/01 tháng (10/2025-09/2026)');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (8, '14-10-2025', 'QĐ số: 01/2025/QĐST-DS ngày 08/07/2025 - TAND KV7 - NA ', 'ông Nguyễn Văn Thọ - Đ/c: Xã Thuần Trung - NA', 'Ông Lê Văn Bốn - Đ/c: Xã Thuần Trung, NA', null, null, null, '', null, '', '', '', '', '', '', '', '2025-11-16', null, '', '', '', 'Số: 232/QĐ-THADS ngày 11/10/2025 trả số tiền: 50.000.000đ');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (7, '14-10-2025', 'Bản án số: 04/2022/DS-ST ngày 22/06/2022 - TAND KV7 - NA', 'ông Võ Văn Hùng - Bà Lê thị Liên - Đ/c: Xã Tân An - NA', 'Ông Hoàng Ngọc Thanh - Đ/c: xã Tân An - NA', null, null, null, '', null, '', '', '', '', '', '', 'QĐ thi hành án theo yêu cầu', '2025-11-16', null, '', '', '', 'Số: 229/QĐ-THADS ngày 11/10/2025 trả lại: 590m2 đất ');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (6, '09-10-2025', 'QĐ công nhận số:  10/2025/QĐ- CNHG-DS ngày  04/06/2025 TAND KV7 - NA', 'Bà Hoàng Thị Kiên - Đ/c: Xã Nghĩa Hành - NA', 'Ông Hồ Văn Hùng -  Đ/c: Xã Nghĩa Hành - NA', null, null, null, '', null, '', '', '', '', '', '', 'QĐ thi hành án theo yêu cầu', '2025-11-15', null, '', '', '', 'Số: 96/QĐ-THADS ngày 06/10/2025 buộc trả lại số tiền: 105.000.000đ');
INSERT INTO so_thu_ly_kiem_soat (id, stt_ngay_tl, ban_an_quyet_dinh, person_who_must_execute, person_to_be_executed, quyet_dinh_uy_thac_di, quyet_dinh_uy_thac_den, quyet_dinh_tha, qd_chua_co_dieu_kien, qd_rut_quyet_dinh_tha, qd_hoan_tha, qd_tiep_tuc_sau_hoan, qd_tam_dinh_chi, qd_tiep_tuc_sau_tam_dinh_chi, qd_dinh_chi, da_thi_hanh_xong, ghi_chu, created_at, updated_at, qd_rut_tha, qd_uy_thac_den, qd_uy_thac_di, qd_tha) VALUES (5, '2025-10-09 00:00:00', 'Bản án số: 08/2022/DS-ST ngày 28/12/2022 - TAND KV7 - NA & BA PT số: 97/2024/DS-PT ngày 10/9/24 TAND tỉnh NA', 'Ông Hoàng Đình Lâm - Bà Nguyễn Thị Thuý; Đ/c: Xã Tân Kỳ, tỉnh Nghệ An', 'Bà Lê Thị Hoa - Đ/c: XÃ Anh Sơn Đông - NA', null, null, null, '', null, '', '', '', '', '', '', 'QĐ thi hành án theo yêu cầu', '2025-11-15', null, '', '', '', 'Số: 36/QĐ-THADS ngày 04/10/2025 buộc trả lại số đất nông nghiệp: 33.726,5m2');




