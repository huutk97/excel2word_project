const USER_API = '/api/admin/users';

let userModal;
let userCurrentPage = 0;
let userPageSize = 10;
let userKeyword = '';

/* ================= LOADER ================= */
function userShowLoader() {
    $('#globalLoader').removeClass('d-none');
}
function userHideLoader() {
    $('#globalLoader').addClass('d-none');
}

/* ================= INIT ================= */
$(document).ready(function () {

    userModal = new bootstrap.Modal(
        document.getElementById('userModal'),
        { backdrop: 'static', keyboard: false }
    );

    $('#userModal').on('hidden.bs.modal', userResetForm);

    $('#userSearchInput').on('keyup', function (e) {
        if (e.key === 'Enter') userSearch();
    });

    $('#userPageSize').on('change', function () {
        userPageSize = parseInt(this.value, 10);
        userLoadPage(0);
    });

    userLoadPage();
});

/* ================= LOAD ================= */
function userLoadPage(page = 0) {
    userCurrentPage = page;

    userShowLoader();
    $.ajax({
        url: USER_API,
        method: 'GET',
        data: {
            page: userCurrentPage,
            size: userPageSize
        },
        success: function (res) {
            userRenderTable(res.content);
            userRenderPageInfo(res);
            userRenderPagination(res);
        },
        error: userHandleError,
        complete: userHideLoader
    });
}

/* ================= SEARCH ================= */
function userSearch() {
    userKeyword = $('#userSearchInput').val().trim();
    userLoadSearch(0);
}

function userLoadSearch(page = 0) {
    userCurrentPage = page;

    userShowLoader();
    $.ajax({
        url: USER_API + '/search',
        method: 'GET',
        data: {
            keyword: userKeyword,
            page: userCurrentPage,
            size: userPageSize
        },
        success: function (res) {
            userRenderTable(res.content);
            userRenderPageInfo(res);
            userRenderPagination(res);
        },
        error: userHandleError,
        complete: userHideLoader
    });
}

/* ================= RENDER ================= */
function userRenderTable(users) {
    const tbody = $('#userTableBody');
    tbody.empty();

    if (!users || users.length === 0) {
        tbody.append(`
      <tr>
        <td colspan="7"
            class="text-center text-muted">
          No data
        </td>
      </tr>
    `);
        return;
    }

    users.forEach(u => {
        tbody.append(`
      <tr>
        <td>${u.id}</td>
        <td>${u.login}</td>
        <td>${u.firstName || ''}</td>
        <td>${u.lastName || ''}</td>
        <td>${u.area || ''}</td>
        <td>${u.authorities.map(r => r.name).join(', ')}</td>
        <td>
          <button class="btn btn-warning btn-sm"
                  onclick="userOpenEdit(${u.id})">
            Edit
          </button>
          <button class="btn btn-danger btn-sm"
                  onclick="userDelete(${u.id})">
            Delete
          </button>
        </td>
      </tr>
    `);
    });
}

function userRenderPageInfo(page) {
    const start = page.number * page.size + 1;
    const end = Math.min(
        (page.number + 1) * page.size,
        page.totalElements
    );
    $('#userPageInfo').text(
        `Hiển thị ${start} – ${end} / ${page.totalElements} | ` +
        `Trang ${page.number + 1} / ${page.totalPages}`
    );
}

function userRenderPagination(res) {

    const current = res.number;
    const total = res.totalPages;

    let html = "";

    if (total <= 1) {
        $('#userPagination').empty();
        return;
    }

    // PREV
    html += `
    <button class="btn btn-sm btn-outline-primary"
            ${res.first ? 'disabled' : ''}
            onclick="userChangePage(${current - 1})">
      ←
    </button>
  `;

    for (let i = 0; i < total; i++) {
        html += `
      <button class="btn btn-sm
        ${i === current ? 'btn-primary' : 'btn-outline-primary'}"
        onclick="userChangePage(${i})">
        ${i + 1}
      </button>
    `;
    }

    // NEXT
    html += `
    <button class="btn btn-sm btn-outline-primary"
            ${res.last ? 'disabled' : ''}
            onclick="userChangePage(${current + 1})">
      →
    </button>
  `;

    $("#userPagination").html(html);
}

/* ================= ACTION ================= */
function userChangePage(page) {
    if (page < 0) return;

    userKeyword
        ? userLoadSearch(page)
        : userLoadPage(page);
}

function userOpenCreate() {
    userResetForm();
    $('#userModalTitle').text('Create User');
    // $('#userPasswordGroup').show();
    // $('#userPassword').prop('required', true);
    userModal.show();
}

function userOpenEdit(id) {
    userShowLoader();

    $.ajax({
        url: USER_API + '/' + id,
        method: 'GET',
        success: function (u) {
            $('#userModalTitle').text('Edit User');
            $('#userId').val(u.id);
            $('#userLogin').val(u.login);
            $('#userFirstName').val(u.firstName || '');
            $('#userLastName').val(u.lastName || '');
            $('#userArea').val(u.area || '');
            // $('#userPassword').val(u.passwordHash || '');

            $('#userRoleAdmin').prop(
                'checked', u.authorities.some(a => a.name === 'ROLE_ADMIN')
            );
            $('#userRoleUser').prop(
                'checked', u.authorities.some(a => a.name === 'ROLE_USER')
            );

            userModal.show();
        },
        error: userHandleError,
        complete: userHideLoader
    });
}

function userSave() {
    const id = $('#userId').val();

    const payload = {
        login: $('#userLogin').val(),
        firstName: $('#userFirstName').val(),
        lastName: $('#userLastName').val(),
        area: $('#userArea').val(),
        password: $('#userPassword').val(),
        roles: userGetRoles()
    };

    userShowLoader();
    $.ajax({
        url: id ? USER_API + '/' + id : USER_API,
        method: id ? 'PUT' : 'POST',
        contentType: 'application/json',
        data: JSON.stringify(payload),
        success: function () {
            userModal.hide();
            userLoadPage(userCurrentPage);
        },
        error: userHandleError,
        complete: userHideLoader
    });
}

function userDelete(id) {
    if (!confirm('Delete this user?')) return;

    userShowLoader();
    $.ajax({
        url: USER_API + '/' + id,
        method: 'DELETE',
        success: function () {
            userLoadPage(userCurrentPage);
        },
        error: userHandleError,
        complete: userHideLoader
    });
}

/* ================= UTIL ================= */
function userGetRoles() {
    const roles = [];
    if ($('#userRoleAdmin').prop('checked')) roles.push('ROLE_ADMIN');
    if ($('#userRoleUser').prop('checked')) roles.push('ROLE_USER');
    return roles;
}

function userResetForm() {
    $('#userId').val('');
    $('#userLogin').val('');
    $('#userPassword').val('');
    $('#userFirstName').val('');
    $('#userLastName').val('');
    $('#userArea').val('');
    $('#userRoleAdmin').prop('checked', false);
    $('#userRoleUser').prop('checked', true);
}

/* ================= ERROR ================= */
function userHandleError(xhr) {
    if (xhr.status === 401) {
        alert('Session expired');
        window.location.href = '/login';
        return;
    }
    alert(xhr.responseJSON?.message || xhr.statusText);
}
