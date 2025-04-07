<%@ page import="java.util.*, model.product, dao.productDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        h2 { margin-bottom: 20px; }
        .actions { margin-bottom: 10px; }
        .actions input[type=text] {
            padding: 6px; width: 200px;
        }
        .actions button {
            padding: 6px 10px;
            margin-left: 5px;
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
        .modal-content input,
        .modal-content textarea {
            width: 100%;
            margin-bottom: 10px;
            padding: 8px;
        }
        .product-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 6px;
            border: 1px solid #ddd;
        }
    </style>
</head>
<body>

<h2>Quản lý sản phẩm</h2>

<div class="actions">
    <form method="get" action="manager-product" style="display: inline" onsubmit="event.preventDefault(); submitSearchProduct(this);">
        <input type="text" name="search" placeholder="Tìm tên sản phẩm..." value="${param.search}">
        <button type="submit">Tìm</button>
    </form>
    <button onclick="openAddProductModal()">➕ Thêm sản phẩm</button>
</div>

<table>
    <tr>
        <th>STT</th><th>Tên</th><th>Mô tả</th><th>Giá</th><th>Ảnh</th><th>Chức năng</th><th>Danh mục</th>
    </tr>
<%
    List<product> list = (List<product>) request.getAttribute("listProduct");
    int stt = 1;
    if (list != null) {
        for (product p : list) {
%>
    <tr>
        <td><%= stt++ %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getDescription() %></td>
        <td><%= p.getPrice() %> VNĐ</td>
        <td><img src="ImageServlet?id=<%= p.getId() %>" class="product-img"></td>
        <td>
            <button onclick="openEditProductModal('<%= p.getId() %>', '<%= p.getName() %>', '<%= p.getDescription() %>', '<%= p.getPrice() %>', 'ImageServlet?id=<%= p.getId() %>', '<%= p.getLabel() %>')">Sửa</button>
            <form onsubmit="event.preventDefault(); deleteProduct(<%= p.getId() %>);" style="display:inline;">
                <button type="submit" onclick="return confirm('Xoá sản phẩm này?')">Xoá</button>
            </form>
        </td>
        <td>
        <% 
            String rawLabel = p.getLabel();
            String labelText = "";
            if ("moive".equals(rawLabel)) labelText = "Mới về";
            else if ("phobien".equals(rawLabel)) labelText = "Phổ biến";
            else if ("noibat".equals(rawLabel)) labelText = "Nổi bật";
            else labelText = "Khác";
        %>
        <%= labelText %>
        </td>

    </tr>
<%
        }
    }
%>
</table>

<!-- Modal Thêm -->
<div id="addProductModal" class="modal">
    <div class="modal-content">
        <h3>Thêm sản phẩm</h3>
        <form id="addProductForm" enctype="multipart/form-data" onsubmit="event.preventDefault(); submitProductForm('addProductForm')">
            <input type="hidden" name="action" value="add">
            <input type="text" name="name" placeholder="Tên sản phẩm" required>
            <textarea name="description" placeholder="Mô tả sản phẩm" required></textarea>
            <input type="number" step="0.01" name="price" placeholder="Giá" required>
            <input type="file" name="image" accept="image/*" onchange="previewAddProductImage(this)" required><br>
            <img id="previewAddProduct" src="#" style="display:none; width:100px; margin-bottom:10px;">
            <label>Phân loại:</label>
            <select name="label" required>
                <option value="phobien">Sản phẩm phổ biến</option>
                <option value="noibat">Sản phẩm nổi bật</option>
                <option value="moive" selected>Sản phẩm mới về</option>
            </select><br><br>
            <button type="submit">Thêm</button>
            <button type="button" onclick="closeModal('addProductModal')">Huỷ</button>
        </form>
    </div>
</div>

<!-- Modal Sửa -->
<div id="editProductModal" class="modal">
    <div class="modal-content">
        <h3>Sửa sản phẩm</h3>
        <form id="editProductForm" enctype="multipart/form-data" onsubmit="event.preventDefault(); submitProductForm('editProductForm')">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" id="edit-product-id" name="id">
            <input type="text" id="edit-product-name" name="name" required>
            <textarea id="edit-product-description" name="description" required></textarea>
            <input type="number" step="0.01" id="edit-product-price" name="price" required>
            <img id="current-product-image" src="" width="100" style="margin-bottom:10px"><br>
            <input type="file" name="image" accept="image/*">
            <label>Phân loại:</label>
            <select name="label" id="edit-product-label" required>
                <option value="phobien">Sản phẩm phổ biến</option>
                <option value="noibat">Sản phẩm nổi bật</option>
                <option value="moive">Sản phẩm mới về</option>
            </select>
            <br><br>
            <button type="submit">Cập nhật</button>
            <button type="button" onclick="closeModal('editProductModal')">Huỷ</button>
        </form>
    </div>
</div>

<script>
    function openAddProductModal() {
        document.getElementById("addProductModal").style.display = "block";
    }

    function openEditProductModal(id, name, desc, price, img, label) {
        document.getElementById("edit-product-id").value = id;
        document.getElementById("edit-product-name").value = name;
        document.getElementById("edit-product-description").value = desc;
        document.getElementById("edit-product-price").value = price;
        document.getElementById("current-product-image").src = img;
        document.getElementById("edit-product-label").value = label;
        document.getElementById("editProductModal").style.display = "block";
    }

    function previewAddProductImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                const img = document.getElementById("previewAddProduct");
                img.src = e.target.result;
                img.style.display = "block";
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    function submitProductForm(formId) {
        const form = document.getElementById(formId);
        const formData = new FormData(form);

        fetch('manager-product', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(html => {
            document.getElementById('products').innerHTML = html;
            closeModal('addProductModal');
            closeModal('editProductModal');
        });
    }

    function deleteProduct(id) {
        const formData = new FormData();
        formData.append("action", "delete");
        formData.append("id", id);

        fetch("manager-product", {
            method: "POST",
            body: formData
        })
        .then(response => response.text())
        .then(html => {
            document.getElementById('products').innerHTML = html;
        });
    }

    function submitSearchProduct(form) {
        const formData = new FormData(form);
        fetch("manager-product?" + new URLSearchParams(formData), {
            method: "GET"
        })
        .then(res => res.text())
        .then(html => {
            document.getElementById("products").innerHTML = html;
        });
    }
</script>

</body>
</html>
