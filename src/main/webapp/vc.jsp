<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.CartItem, model.Cart" %>
<html>
<head>
  <title>Trang chủ</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="p-4">

<h2 class="mb-4">🛍️ Danh sách sản phẩm</h2>

<form method="post" action="add-to-cart" class="mb-2">
  <input type="hidden" name="productId" value="1">
  <input type="hidden" name="quantity" value="1">
  <button class="btn btn-success">Mua Sản phẩm 1</button>
</form>

<form method="post" action="add-to-cart" class="mb-4">
  <input type="hidden" name="productId" value="2">
  <input type="hidden" name="quantity" value="1">
  <button class="btn btn-success">Mua Sản phẩm 2</button>
</form>

<!-- Nút mở popup giỏ hàng -->
<button class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#cartModal">
  🛒 Xem giỏ hàng

</button>
 <jsp:include page="giohang.jsp" />  


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
