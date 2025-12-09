const token = localStorage.getItem("jwt");

if (token) {
    window.location.href = "/home";
}

/* ================= LOGIN ================= */
function doLogin() {

    fetch("/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        })
    })
        .then(res => {
            if (!res.ok) throw new Error("Login failed");
            return res.json();
        })
        .then(data => {

            // 1️⃣ Lưu JWT
            localStorage.setItem("jwt", data.token);

            // 2️⃣ Gọi API user info
            return fetch("/api/user/info", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + data.token
                }
            });
        })
        .then(res => {
            if (!res.ok) throw new Error("Cannot load user info");
            return res.json();
        })
        .then(userInfo => {

            // 3️⃣ Lưu thông tin user (tuỳ bạn cần gì)
            localStorage.setItem("userInfo", JSON.stringify(userInfo));
            localStorage.setItem("userId", userInfo.id);
            localStorage.setItem("account", userInfo.username);
            localStorage.setItem("roles", JSON.stringify(userInfo.roles));

            // 4️⃣ Redirect sang home
            window.location.href = "/home";
        })
        .catch(err => {
            console.error(err);
            document.getElementById("error").classList.remove("d-none");
            document.getElementById("error").innerText =
                "Invalid username or password";
        });
}
