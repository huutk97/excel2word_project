/* ================= JWT CHECK ================= */
const jwt = localStorage.getItem("jwt");
if (!jwt) {
    window.location.href = "/login";
}

/* ====== Flatpickr cho tất cả input ngày ====== */
flatpickr("#searchFrom", { dateFormat: "d-m-Y" });
flatpickr("#searchTo", { dateFormat: "d-m-Y" });
flatpickr("#sttNgayTl", { dateFormat: "d-m-Y" });


/* ====== MỞ MODAL THÊM ====== */
function openCreateModal() {
    $("#modalTitle").text("Thêm mới");
    $("#id").val("");
    $("#editModal input").val("");
    $("#editModal").modal("show");
}


/* ====== LẤY DỮ LIỆU EDIT ====== */
function openEditModal(id) {
    $.get("/api/so-thu-ly/" + id, function (d) {
        $("#id").val(d.id);
        $("#sttNgayTl").val(d.sttNgayTl);
        $("#banAnQuyetDinh").val(d.banAnQuyetDinh);
        $("#orderNumber").val(d.orderNumber);
        $("#personWhoMustExecute").val(d.personWhoMustExecute);
        $("#personToBeExecuted").val(d.personToBeExecuted);
        $("#qdUyThacDi").val(d.qdUyThacDi);
        $("#qdUyThacDen").val(d.qdUyThacDen);
        $("#qdTha").val(d.qdTha);
        $("#ndThiHanh").val(d.ndThiHanh);
        $("#qdChuaCoDieuKien").val(d.qdChuaCoDieuKien);
        $("#qdRutTha").val(d.qdRutTha);
        $("#qdHoanTha").val(d.qdHoanTha);
        $("#qdTiepTucSauHoan").val(d.qdTiepTucSauHoan);
        $("#qdTamDinhChi").val(d.qdTamDinhChi);
        $("#qdTiepTucSauTamDinhChi").val(d.qdTiepTucSauTamDinhChi);
        $("#qdDinhChi").val(d.qdDinhChi);
        $("#daThiHanhXong").val(d.daThiHanhXong);
        $("#ghiChu").val(d.ghiChu);
        $("#veThoiHanGuiQD").val(d.veThoiHanGuiQD);
        $("#veCanCuBanHanhQD").val(d.veCanCuBanHanhQD);
        $("#veThamQuyenBanHanhQD").val(d.veThamQuyenBanHanhQD);
        $("#veHinhThucQD").val(d.veHinhThucQD);
        $("#veNoiDungQD").val(d.veNoiDungQD);
        $("#noiDungKhac").val(d.noiDungKhac);
        $("#quanDiemKSV").val(d.quanDiemKSV);
        $("#maPhieu").val(d.maPhieu);
        $("#vienKsndCap").val(d.vienKsndCap);
        $("#khuVuc").val(d.khuVuc);
        $("#editModal").modal("show");
    });
}


/* ====== LƯU ====== */
function saveItem() {
    showLoader();
    let data = {
        id: $("#id").val(),
        sttNgayTl: $("#sttNgayTl").val(),
        banAnQuyetDinh: $("#banAnQuyetDinh").val(),
        orderNumber: $("#orderNumber").val(),
        personWhoMustExecute: $("#personWhoMustExecute").val(),
        personToBeExecuted: $("#personToBeExecuted").val(),
        qdUyThacDi: $("#qdUyThacDi").val(),
        qdUyThacDen: $("#qdUyThacDen").val(),
        qdTha: $("#qdTha").val(),
        ndThiHanh: $("#ndThiHanh").val(),
        qdChuaCoDieuKien: $("#qdChuaCoDieuKien").val(),
        qdRutTha: $("#qdRutTha").val(),
        qdHoanTha: $("#qdHoanTha").val(),
        qdTiepTucSauHoan: $("#qdTiepTucSauHoan").val(),
        qdTamDinhChi: $("#qdTamDinhChi").val(),
        qdTiepTucSauTamDinhChi: $("#qdTiepTucSauTamDinhChi").val(),
        qdDinhChi: $("#qdDinhChi").val(),
        daThiHanhXong: $("#daThiHanhXong").val(),
        ghiChu: $("#ghiChu").val(),
        veThoiHanGuiQD: $("#veThoiHanGuiQD").val(),
        veCanCuBanHanhQD: $("#veCanCuBanHanhQD").val(),
        veThamQuyenBanHanhQD: $("#veThamQuyenBanHanhQD").val(),
        veHinhThucQD: $("#veHinhThucQD").val(),
        veNoiDungQD: $("#veNoiDungQD").val(),
        noiDungKhac: $("#noiDungKhac").val(),
        maPhieu: $("#maPhieu").val(),
        vienKsndCap: $("#vienKsndCap").val(),
        khuVuc: $("#khuVuc").val(),
        quanDiemKSV: $("#quanDiemKSV").val()
    };

    $.ajax({
        url: "/api/so-thu-ly",
        method: data.id ? "PUT" : "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function () {
            searchData(0); // reload bằng AJAX, không reload page
            $("#editModal").modal("hide");
        }, complete: function () {
            hideLoader();   // 🔥 luôn chạy
        }
    });
}


/* ====== XÓA ====== */
function deleteItem(id) {
    if (!confirm("Bạn có chắc muốn xóa?")) return;
    showLoader();
    $.ajax({
        url: "/api/so-thu-ly/" + id,
        method: "DELETE",
        success: function () {
            searchData(0);
        }, complete: function () {
            hideLoader();   // 🔥 luôn chạy
        }
    });
}

function deleteMultiple() {
    let ids = [];

    $(".row-check:checked").each(function() {
        ids.push($(this).val());
    });

    if (ids.length === 0) {
        alert("Vui lòng chọn ít nhất 1 hàng để xóa!");
        return;
    }

    if (!confirm("Bạn có chắc muốn xóa " + ids.length + " bản ghi này?")) return;

    showLoader();

    $.ajax({
        url: "/api/so-thu-ly/delete-multiple",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(ids),
        success: function () {
            searchData(0);
        },
        complete: function () {
            hideLoader();
        }
    });
}


/* ====== EXPORT ====== */
function exportExcel() {
    showLoader();

    let begin = $("#searchFrom").val();
    let end = $("#searchTo").val();
    let searchAccount = $("#searchAccount").val();

    begin = begin ? formatDateToYMD(begin) : "";
    end = end ? formatDateToYMD(end) : "";

    const token = localStorage.getItem("jwt");

    $.ajax({
        url: "/api/so-thu-ly/export",
        method: "GET",
        data: {
            beginDate: begin,
            endDate: end,
            account: searchAccount
        },
        xhrFields: {
            responseType: "blob"   // 🔥 bắt buộc để download file
        },
        beforeSend: function (xhr) {
            if (token) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            }
        },
        success: function (data, status, xhr) {

            // ✅ Lấy tên file từ header nếu backend set
            let filename = "so-thu-ly.xlsx";
            const disposition = xhr.getResponseHeader("Content-Disposition");
            if (disposition && disposition.indexOf("attachment") !== -1) {
                const match = disposition.match(/filename="?([^"]+)"?/);
                if (match && match[1]) {
                    filename = match[1];
                }
            }

            // ✅ Tạo blob để download
            const blob = new Blob([data], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            });

            const link = document.createElement("a");
            link.href = window.URL.createObjectURL(blob);
            link.download = filename;

            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        },
        error: function () {
            alert("❌ Không thể export Excel!");
        },
        complete: function () {
            hideLoader(); // ✅ loader luôn tắt
        }
    });
}


/* ====== DOWNLOAD DOC ====== */
function showloadFileDoc(id) {
    if (!confirm("Bạn có muốn download file DOC?")) return;

    showLoader();

    $.ajax({
        url: "/api/so-thu-ly/download-doc/" + id,
        method: "GET",
        xhrFields: {
            responseType: 'blob'   // nhận file dạng blob
        },
        success: function (data, status, xhr) {

            // Lấy tên file từ header nếu có
            let filename = "";
            let disposition = xhr.getResponseHeader('Content-Disposition');
            if (disposition && disposition.indexOf('attachment') !== -1) {
                let match = disposition.match(/filename="?([^"]+)"?/);
                if (match && match[1]) filename = match[1];
            }
            if (!filename) filename = "download.docx";

            // Tạo Blob
            let blob = new Blob([data], {
                type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            });

            // Tạo link ảo
            let link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = filename;

            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            hideLoader();
        },
        error: function () {
            alert("Không thể download file!");
            hideLoader();
        }
    });
}


/* ====== SEARCH AJAX + PAGINATION ====== */
function searchData(page = 0) {
    showLoader();

    let begin = $("#searchFrom").val();
    let end = $("#searchTo").val();
    let searchAccount = $("#searchAccount").val();

    begin = begin ? formatDateToYMD(begin) : null;
    end = end ? formatDateToYMD(end) : null;

    $.ajax({
        url: "/api/so-thu-ly/search",
        method: "GET",
        data: {
            beginDate: begin,
            endDate: end,
            page: page,
            account: searchAccount,
            size: $("#pageSize").val()
        },
        success: function (res) {
            renderTable(res.content);
            renderPaginationSoThuLy(res);
            var totalElements = res.totalElements;
            $("#totalCountLabel").text("Tổng số bản ghi: " + totalElements);
        }, complete: function () {
            hideLoader();   // 🔥 luôn chạy
        }
    });
}

function formatDateToYMD(dateStr) {
    if (!dateStr) return null; // nếu null hoặc empty thì trả về null

    // Tách chuỗi: dd-MM-yyyy
    const parts = dateStr.split("-");
    if (parts.length !== 3) return null;

    const dd = parts[0];
    const mm = parts[1];
    const yyyy = parts[2];

    return `${yyyy}-${mm}-${dd}`;
}


function renderTable(list) {
    let html = "";
    let index = 1;

    list.forEach(item => {
        html += `
                <tr>
                    <td class="text-center">
                        <input type="checkbox" class="row-check" value="${item.id}">
                    </td>
                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${index++}</span>
                    </td>
                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.formatDateNgayTl || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.account || ""}</span>
                    </td>
                    
                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.banAnQuyetDinh || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.personWhoMustExecute || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.personToBeExecuted || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdUyThacDi || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdUyThacDen || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdTha || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.ndThiHanh || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdChuaCoDieuKien || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdRutTha || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdHoanTha || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdTiepTucSauHoan || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdTamDinhChi || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdTiepTucSauTamDinhChi || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.qdDinhChi || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.daThiHanhXong || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.ghiChu || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.veThoiHanGuiQD || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.veCanCuBanHanhQD || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.veThamQuyenBanHanhQD || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.veHinhThucQD || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.veNoiDungQD || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.noiDungKhac || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.quanDiemKSV || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.khuVuc || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.vienKsndCap || ""}</span>
                    </td>

                    <td>
                        <button class="btn-copy" onclick="copyTdValue(this)"><i class="fa fa-copy"></i></button>
                        <span>${item.maPhieu || ""}</span>
                    </td>
                    <td class="sticky-action">
                        <button class="btn btn-warning btn-sm" onclick="openEditModal(${item.id})">Sửa</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteItem(${item.id})">Xóa</button>
                        <button class="btn btn-info btn-sm" onclick="showloadFileDoc(${item.id})"><i class="fa fa-download"></i> .Doc</button>
                        <button class="btn btn-secondary btn-sm mt-1" onclick="copyRow(${item.id})">Copy hàng</button>
                    </td>
                </tr>
            `;
    });

    $("#tableBody").html(html);
}

function copyRow(id) {
    if (!confirm("Bạn có muốn copy hàng này và tạo một bản ghi mới?")) return;
    showLoader();

    $.ajax({
        url: "/api/so-thu-ly/copy/" + id,   // endpoint copy (sẽ tạo mới 1 bản ghi)
        method: "POST",
        success: function () {
            // sau khi copy xong, load lại page 0 để thấy hàng mới trên đầu
            searchData(0);
        },
        error: function () {
            alert("Có lỗi xảy ra khi copy hàng!");
        }, complete: function () {
            hideLoader();   // 🔥 luôn chạy
        }
    });
}

function toggleCheckAll(cb) {
    $(".row-check").prop("checked", cb.checked);
}

function copyTdValue(btn) {
    const td = btn.parentElement;
    const value = td.querySelector("span").innerText.trim();

    navigator.clipboard.writeText(value).then(() => {

        // Tạo tooltip
        let tooltip = document.createElement("div");
        tooltip.className = "copy-tooltip";
        tooltip.innerText = "Đã copy!";

        // Thêm vào body để nó tách khỏi td
        document.body.appendChild(tooltip);

        // Lấy vị trí button để đặt tooltip đúng chỗ
        const rect = btn.getBoundingClientRect();

        tooltip.style.left = rect.left + window.scrollX + "px";
        tooltip.style.top = rect.top + window.scrollY - 30 + "px"; // nằm trên nút 30px

        // hiệu ứng fade
        setTimeout(() => {
            tooltip.classList.add("fade-out");
        }, 700);

        // xoá tooltip
        setTimeout(() => {
            tooltip.remove();
        }, 1000);
    });
}


function renderPaginationSoThuLy(res) {
    let current = res.number;
    let total = res.totalPages;

    let html = "";

    for (let i = 0; i < total; i++) {
        html += `
                <button class="btn btn-sm ${i === current ? "btn-primary" : "btn-outline-primary"}"
                        onclick="searchData(${i})">
                    ${i + 1}
                </button>
            `;
    }

    $("#paginationSoThuLy").html(html);
}


/* ====== LOAD LẦN ĐẦU ====== */
$(document).ready(function () {
    searchData(0);
});


/* ====== HIỆU ỨNG RIPPLE KHI CLICK BUTTON ====== */
document.addEventListener("click", function (e) {
    const target = e.target;

    // nếu click là button hoặc nằm trong button
    const btn = target.closest(".btn, .btn-copy");
    if (!btn) return;

    const rect = btn.getBoundingClientRect();
    const circle = document.createElement("span");
    const diameter = Math.max(rect.width, rect.height);
    const radius = diameter / 2;

    circle.style.width = circle.style.height = `${diameter}px`;
    circle.style.left = `${e.clientX - rect.left - radius}px`;
    circle.style.top = `${e.clientY - rect.top - radius}px`;
    circle.classList.add("ripple");

    // Xoá ripple cũ nếu có
    const oldRipple = btn.querySelector(".ripple");
    if (oldRipple) {
        oldRipple.remove();
    }

    btn.appendChild(circle);

    // ripple tự mất sau khi animation xong
    setTimeout(() => circle.remove(), 600);
});
//
// // Kiểm tra token có còn hiệu lực không
// function checkGoogleAuth() {
//     const valid = localStorage.getItem("google_auth_valid");
//     const expireTime = localStorage.getItem("google_auth_expire");
//
//     if (valid && expireTime) {
//         const now = new Date().getTime();
//         if (now < parseInt(expireTime)) {
//             return true; // token còn hạn → cho vào hệ thống
//         }
//     }
//     return false;
// }
//
// async function submitCodeGoogleAuth() {
//     const code = $("#googleCode").val();
//     const token = localStorage.getItem("jwt");
//
//     if (!code) {
//         alert("Vui lòng nhập mã Google!");
//         return;
//     }
//
//     $.ajax({
//         url: "/api/config/authGoogle",
//         method: "GET",
//         data: { code },
//         beforeSend: function (xhr) {
//             if (token) {
//                 xhr.setRequestHeader("Authorization", "Bearer " + token);
//             }
//         },
//         success: function () {
//             localStorage.setItem("google_auth_valid", "true");
//             localStorage.setItem(
//                 "google_auth_expire",
//                 (Date.now() + 24 * 60 * 60 * 1000).toString()
//             );
//
//             $("#authModal").hide();
//         },
//         error: function () {
//             alert("❌ Mã Google không đúng! Nhập lại.");
//         }
//     });
// }
//
// // Khi trang load → kiểm tra token trong localStorage
// document.addEventListener("DOMContentLoaded", () => {
//     if (checkGoogleAuth()) {
//         // Không cần nhập lại mã
//         document.getElementById("authModal").style.display = "none";
//         // document.getElementById("mainApp").style.display = "block";
//     } else {
//         // Yêu cầu nhập mã lại
//         document.getElementById("authModal").style.display = "flex";
//     }
// });