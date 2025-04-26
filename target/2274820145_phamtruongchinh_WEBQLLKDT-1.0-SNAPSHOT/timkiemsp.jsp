<%@ page import="java.util.List, model.product, dao.productDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>


<%
  String keyword = request.getParameter("keyword");
  List<product> products = productDAO.searchByKeyword(keyword);
%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Kết quả tìm kiếm</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .product-card {
      transition: transform 0.3s ease, box-shadow 0.3s ease;
    }
    .product-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    }
    .product-card img {
      height: 160px;
      object-fit: cover;
    }
    .product-card h5 {
      min-height: 48px;
    }
    .product-card a {
      text-decoration: none;
    }
    .text-truncate {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .breadcrumb {
      background: #FBC02D;
      padding: 15px 30px;
      margin-bottom: 30px;
    }
    .breadcrumb a {
      text-decoration: none;
      color: inherit;
    }
    .mb-4 {
      text-align: center;
    }
  </style>
</head>
<body class="container py-5">
  <div class="breadcrumb">
    <a href="index.jsp">Trang chủ</a> &nbsp; &gt; &nbsp;
    <a href="#">Kết quả tìm kiếm</a> &nbsp; &gt; &nbsp;
    <strong><%= keyword != null ? keyword : "" %></strong>
  </div>

  <h3 class="mb-4">Có <%= products.size() %> kết quả tìm kiếm phù hợp</h3>

  <div class="row">
  <% for (product p : products) { %>
    <div class="col-md-3 mb-4">
      <div class="product-card border rounded p-3 h-100">
        <a href="product-detail.jsp?id=<%= p.getId() %>">
          <img src="ImageServlet?id=<%= p.getId() %>" alt="<%= p.getName() %>" class="w-100 mb-2">
          <h5 class="text-dark text-truncate"><%= p.getName() %></h5>
        </a>
        <p class="fw-bold text-danger"><%= p.getPrice() %>₫</p>
        <a href="#" class="btn btn-warning w-100">Mua ngay</a>
      </div>
    </div>
  <% } %>
  </div>
</body>
<jsp:include page="footer.jsp" />

</html>