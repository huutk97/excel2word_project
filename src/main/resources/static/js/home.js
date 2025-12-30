/* ================= JWT CHECK ================= */
const token = localStorage.getItem("jwt");

if (!token) {
    window.location.href = "/login";
}

/* gắn JWT cho toàn bộ ajax (nếu còn dùng jQuery) */
$.ajaxSetup({
    beforeSend: function (xhr) {
        if (token) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
    }
});

/* ================= DECODE JWT ================= */
let payload;
try {
    payload = JSON.parse(atob(token.split(".")[1]));
    if (isExpired(payload)) logout();
} catch (e) {
    logout();
}

function isExpired(payload) {
    return payload.exp && payload.exp < Date.now() / 1000;
}

const roles = payload.authorities || payload.roles || [];

/* ================= ROLE MENU ================= */
function showMenu(id) {
    const el = document.getElementById(id);
    if (!el) return;
    el.classList.remove("d-none");
}

if (roles.includes("ROLE_ADMIN")) {
    showMenu("menuUser");
    showMenu("menuSoThuLy");
}

if (roles.includes("ROLE_USER")) {
    showMenu("menuSoThuLy");
}

/* ================= DEFAULT TAB ================= */
if (roles.includes("ROLE_ADMIN")) {
    const userTabBtn = document.getElementById("tabUserBtn");
    if (userTabBtn) userTabBtn.click();
}

const isAdmin = roles.includes("ROLE_ADMIN");
if (isAdmin) {
    loadScript("/js/user.js");
}

function loadScript(src) {
    const script = document.createElement("script");
    script.src = src;
    script.type = "text/javascript";
    script.defer = true;
    document.body.appendChild(script);
}

/* ================= ACCOUNT SELECT ================= */
function handleAccountAction(select) {
    const action = select.value;

    if (action === "reset") {
        openResetPasswordModal();
    }

    if (action === "logout") {
        logout();
    }

    // reset select về trạng thái ban đầu
    select.selectedIndex = 0;
}

/* ================= RESET PASSWORD MODAL ================= */
function openResetPasswordModal() {
    const modalEl = document.getElementById("resetPasswordModal");
    if (!modalEl) return;

    clearResetPasswordForm();

    const modal = new bootstrap.Modal(modalEl);
    modal.show();
}

function clearResetPasswordForm() {
    const oldPwd = document.getElementById("oldPassword");
    const newPwd = document.getElementById("newPassword");
    const confirmPwd = document.getElementById("confirmPassword");

    if (oldPwd) oldPwd.value = "";
    if (newPwd) newPwd.value = "";
    if (confirmPwd) confirmPwd.value = "";
}

/* ================= LOGOUT ================= */
function logout() {
    fetch("/api/auth/logout", {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token
        }
    }).finally(() => {
        localStorage.removeItem("jwt");
        window.location.href = "/login";
    });
}

function submitResetPassword() {

    const modal = document.getElementById("resetPasswordModal");

    const oldPassword = modal.querySelector("#oldPassword").value;
    const newPassword = modal.querySelector("#newPassword").value;
    const confirmPassword = modal.querySelector("#confirmPassword").value;

    if (!oldPassword || !newPassword) {
        alert("Vui lòng nhập đầy đủ");
        return;
    }

    if (newPassword !== confirmPassword) {
        alert("Mật khẩu xác nhận không khớp");
        return;
    }

    fetch("/api/user/reset-password", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("jwt")
        },
        body: JSON.stringify({ oldPassword, newPassword })
    })
        .then(res => {
            if (!res.ok) throw new Error("Reset failed");
            alert("✅ Reset password thành công");

            bootstrap.Modal.getInstance(modal).hide();
        })
        .catch(err => {
            console.error("RESET ERROR:", err);
            alert("❌ Reset thất bại");
        });
}

const username =
    payload.username ||
    payload.user_name ||
    payload.sub ||
    payload.email ||
    "User";

// set vào option
const accountNameEl = document.getElementById("accountName");
if (accountNameEl) {
    accountNameEl.textContent = "👤 " + username;
}