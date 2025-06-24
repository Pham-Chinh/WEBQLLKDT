<%@ page import="java.util.*, model.taikhoan, dao.accountDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        h2 { margin-bottom: 20px; }
        .actions { margin-bottom: 10px; display: flex; gap: 10px; align-items: center; }
        .actions input[type=text] {
            padding: 6px; width: 200px;
        }
        .actions button {
            padding: 6px 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
        }
        th { background-color: #eee; }
        .modal {
            display: none;
            position: fixed;
            z-index: 999;
            left: 0; top: 0;
            width: 100%; height: 100%;
            background-color: rgba(0,0,0,0.5);
        }
        .modal-content {
            background-color: #fff;
            margin: 10% auto;
            padding: 20px;
            width: 400px;
            border-radius: 8px;
        }
        .modal-content input {
            width: 100%;
            margin-bottom: 10px;
            padding: 8px;
        }
    </style>
</head>
<body>

<h2>Quản lý tài khoản</h2>

<div class="actions">
    <form method="get" onsubmit="event.preventDefault(); submitSearch(this);" style="display:inline;">
        <input type="text" name="search" placeholder="Tìm username..." value="${param.search}">
        <button type="submit">Tìm</button>
    </form>
    <button onclick="openAddModal()">➕ Thêm tài khoản</button>
</div>

<table>
    <tr>
        <th>STT</th><th>Username</th><th>Password</th><th>Vai trò</th><th>Chức năng</th>
    </tr>
<%
    List<taikhoan> list = (List<taikhoan>) request.getAttribute("listAccount");
    int stt = 1;
    if (list != null) {
        for (taikhoan acc : list) {
%>
    <tr>
        <td><%= stt++ %></td>
        <td><%= acc.getUsername() %></td>
        <td><%= acc.getPassword()%></td>
        <td><%= acc.getRole() %></td>
        <td>
            <button onclick="openEditModal('<%= acc.getId() %>', '<%= acc.getUsername() %>', '<%= acc.getPassword() %>', '<%= acc.getRole() %>')">Sửa</button>
            <form onsubmit="event.preventDefault(); deleteAccount(<%= acc.getId() %>);" style="display:inline;">
                <button type="submit" onclick="return confirm('Xoá tài khoản này?')">Xoá</button>
            </form>
        </td>
    </tr>
<%
        }
    }
%>
</table>

<!-- Modal thêm -->
<div id="addModal" class="modal">
    <div class="modal-content">
        <h3>Thêm tài khoản</h3>
        <form id="addForm" onsubmit="event.preventDefault(); submitForm('addForm')">
            <input type="hidden" name="action" value="add">
            <input type="text" name="username" placeholder="Username" required>
            <input type="text" name="password" placeholder="Password" required>
            <select name="role">
                <option value="user">User</option>
                <option value="admin">Admin</option>
            </select><br><br>
            <button type="submit">Thêm</button>
            <button type="button" onclick="closeModal('addModal')">Huỷ</button>
        </form>
    </div>
</div>

<!-- Modal sửa -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <h3>Sửa tài khoản</h3>
        <form id="editForm" onsubmit="event.preventDefault(); submitForm('editForm')">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" id="edit-id" name="id">
            <input type="text" id="edit-username" name="username" required>
            <input type="text" id="edit-password" name="password" required>
            <select name="role" id="edit-role">
                <option value="user">User</option>
                <option value="admin">Admin</option>
            </select><br><br>
            <button type="submit">Cập nhật</button>
            <button type="button" onclick="closeModal('editModal')">Huỷ</button>
        </form>
    </div>
</div>

<script>
    function openAddModal() {
        document.getElementById("addModal").style.display = "block";
    }

    function openEditModal(id, username, password, role) {
        document.getElementById("edit-id").value = id;
        document.getElementById("edit-username").value = username;
        document.getElementById("edit-password").value = password;
        document.getElementById("edit-role").value = role;
        document.getElementById("editModal").style.display = "block";
    }

    function closeModal(id) {
        document.getElementById(id).style.display = "none";
    }

    function submitForm(formId) {
        const form = document.getElementById(formId);
        const formData = new FormData(form);

        fetch("manager-account", {
            method: "POST",
            body: formData
        })
        .then(res => res.text())
        .then(html => {
            document.getElementById("accounts").innerHTML = html;
            closeModal("addModal");
            closeModal("editModal");
        });
    }

    function deleteAccount(id) {
        const formData = new FormData();
        formData.append("action", "delete");
        formData.append("id", id);

        fetch("manager-account", {
            method: "POST",
            body: formData
        })
        .then(res => res.text())
        .then(html => {
            document.getElementById("accounts").innerHTML = html;
        });
    }

    function submitSearch(form) {
        const formData = new FormData(form);
        fetch("manager-account?" + new URLSearchParams(formData), {
            method: "GET"
        })
        .then(res => res.text())
        .then(html => {
            document.getElementById("accounts").innerHTML = html;
        });
    }

    window.onclick = function(event) {
        ['addModal', 'editModal'].forEach(function(id) {
            var modal = document.getElementById(id);
            if (event.target === modal) modal.style.display = "none";
        });
    };
</script>

</body>
</html>
