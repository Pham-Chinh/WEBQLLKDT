<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.*, java.util.*" %>
<html>
<head>
    <title>Giỏ hàng</title>
</head>
<body>

<h2>Giỏ hàng của bạn</h2>

<%
    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null || cart.getItems().isEmpty()) {
%>
    <p>Giỏ hàng trống.</p>
<%
    } else {
%>

<table border="1" cellpadding="10">
    <tr>
        <th>Sản phẩm</th>
        <th>Đơn giá</th>
        <th>Số lượng</th>
        <th>Thành tiền</th>
        <th>Xóa</th>
    </tr>

<%
    for (CartItem item : cart.getItems()) {
        product p = item.getProduct();
%>
    <tr>
        <td><%= p.getName() %></td>
        <td><%= p.getPrice() %></td>
        <td>
            <form action="UpdatetoCart" method="post">
                <input type="hidden" name="id" value="<%= p.getId() %>">
                <input type="number" name="qty" value="<%= item.getQuantity() %>" min="1">
                <button type="submit">Cập nhật</button>
            </form>
        </td>
        <td><%= p.getPrice() * item.getQuantity() %></td>
        <td>
            <form action="RemoveCartServlet" method="post">
                <input type="hidden" name="id" value="<%= p.getId() %>">
                <button type="submit">Xóa</button>
            </form>
        </td>
    </tr>
<%
    }
%>
    <tr>
        <td colspan="3"><strong>Tổng tiền:</strong></td>
        <td colspan="2"><strong><%= cart.getTotalAmount() %></strong></td>
    </tr>
</table>

<%
    }
%>

<br>
<a href="index.jsp">Quay lại mua sắm</a>

</body>
</html>
