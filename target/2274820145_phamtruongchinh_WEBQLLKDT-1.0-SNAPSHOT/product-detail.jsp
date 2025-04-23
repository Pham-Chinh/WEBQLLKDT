<%@ page import="model.product, dao.productDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<%
    int id = -1;
    product p = null;
    try {
        id = Integer.parseInt(request.getParameter("id"));
        p = productDAO.getProductById(id);
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><%= (p != null ? p.getName() : "Không tìm thấy sản phẩm") %></title>

    <!-- Link CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="css/vendor.css">
    <link rel="stylesheet" type="text/css" href="style.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;700&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Nunito', sans-serif;
            background-color: #fffef8;
        }

        .breadcrumb {
            background: #ddd;
            padding: 15px 30px;
            margin-bottom: 30px;
        }

        .product-detail-container {
            display: flex;
            gap: 30px;
            max-width: 1200px;
            margin: auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        }

        .product-detail-container img {
            width: 100%;
            max-width: 350px;
            border: 1px solid #eee;
        }

        .product-info {
            flex: 1;
        }

        .product-info h1 {
            font-size: 28px;
            margin-bottom: 20px;
        }

        .product-price {
            color: red;
            font-size: 26px;
            font-weight: bold;
        }

        .btn-yellow {
            background-color: #ffc107;
            border: none;
            padding: 10px 25px;
            font-weight: bold;
            color: black;
        }

        .btn-yellow:hover {
            background-color: #e6b400;
        }

        .buy-section {
            display: flex;
            gap: 15px;
            margin-top: 20px;
        }

        .btn-blue {
            background-color: #0033cc;
            color: white;
            border: none;
            padding: 10px 25px;
            font-weight: bold;
        }

        .btn-blue:hover {
            background-color: #001a66;
        }

        .tag-box {
            background: #f1f1f1;
            padding: 15px;
            border-left: 5px solid #ffc107;
            margin-top: 20px;
        }

        .tag-box ul {
            padding-left: 1.2rem;
        }

        .quantity-box {
            display: flex;
            align-items: center;
            gap: 10px;
            margin: 15px 0;
        }

        .quantity-box input {
            width: 50px;
            text-align: center;
        }
        .breadcrumb{
            background:#FBC02D;
        }
        .breadcrumb a {
    text-decoration: none;
    color: inherit;
}

    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="breadcrumb">
    <a href="index.jsp">Trang chủ</a> &nbsp; &gt; &nbsp;

    <%
        if (p != null) {
            String label = p.getLabel();
            String labelText = "";
            switch (label) {
                case "moive": labelText = "Sản phẩm mới về"; break;
                case "phobien": labelText = "Sản phẩm phổ biến"; break;
                case "noibat": labelText = "Sản phẩm nổi bật"; break;
            }
    %>
        <a href="index.jsp#<%= label %>"><%= labelText %></a>
    <%
        } else {
    %>
        <span>Không tìm thấy danh mục sản phẩm</span>
    <%
        }
    %>

    &nbsp; &gt; &nbsp;
    <strong><%= (p != null ? p.getName() : "Không tìm thấy") %></strong>
</div>




<% if (p != null) { %>
<div class="product-detail-container">
    <div class="product-image">
        <img src="ImageServlet?id=<%= p.getId() %>" alt="<%= p.getName() %>">
    </div>
    <div class="product-info">
        <h1><%= p.getName() %></h1>
        <p class="product-price"><%= p.getPrice() %> VNĐ</p>

        <div class="quantity-box">
            <label for="quantity">Số lượng:</label>
            <input type="number" id="quantity" name="quantity" value="1" min="1">
        </div>

        <div class="buy-section">
            <button class="btn-yellow">Thêm vào giỏ</button>
            <button class="btn-blue">Mua ngay</button>
        </div>

        <div class="mt-4">
            <p><strong>Mô tả:</strong> <%= p.getDescription() %></p>
            <p><strong>Danh mục:</strong>
                <%
                    String label = p.getLabel();
                    switch (label) {
                        case "moi": out.print("Mới về"); break;
                        case "phobien": out.print("Phổ biến"); break;
                        case "noibat": out.print("Nổi bật"); break;
                        default: out.print("Khác"); break;
                    }
                %>
            </p>
        </div>

        <div class="tag-box">
            <h5>Cam kết</h5>
            <ul>
                <li>✔ Kiểm tra sản phẩm trước khi giao</li>
                <li>✔ Tư vấn tận tình, chu đáo</li>
                <li>✔ Bảo hành đầy đủ, nhanh chóng</li>
            </ul>
        </div>

        <div class="tag-box mt-3">
            <h5>Khuyến mãi</h5>
            <ul>
                <li>🚚 Miễn phí giao hàng với đơn > 2tr</li>
                <li>🎁 Quà tặng khi mua đơn hàng lớn</li>
            </ul>
        </div>
    </div>
</div>
<% } else { %>
<div class="container text-center">
    <p style="color:red;">❌ Không tìm thấy sản phẩm với ID này.</p>
    <a href="index.jsp" class="btn btn-secondary">⬅ Quay lại trang chủ</a>
</div>
<% } %>

<script>
    window.addEventListener("load", function () {
        const preloader = document.querySelector(".preloader-wrapper");
        if (preloader) {
            preloader.style.display = "none";
        }
    });
</script>
</body>
</html>
