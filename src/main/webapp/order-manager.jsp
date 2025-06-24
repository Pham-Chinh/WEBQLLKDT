<%@ page import="java.util.*, model.Order, dao.OrderDAO, java.text.SimpleDateFormat, java.text.NumberFormat, java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Đơn hàng</title>
    <style>
        /* CSS được copy từ file manager-product.jsp của bạn và chỉnh sửa lại */
        body { font-family: sans-serif; }
        h2 { margin-bottom: 20px; }
        .actions { margin-bottom: 10px; }
        .actions input[type=text] { padding: 6px; width: 200px; }
        .actions button, table button {
            background-color: gold; color: black; border: 2px solid #caa100;
            border-radius: 4px; cursor: pointer; padding: 6px 10px;
        }
        table button:hover, .actions button:hover {
            background-color: #e6c200; border-color: #a88900;
        }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }
        th { background-color: #eee; }
        .badge { padding: .25em .6em; font-size: .75em; font-weight: 700; line-height: 1; text-align: center; white-space: nowrap; vertical-align: baseline; border-radius: .25rem; }
        .bg-success { background-color: #198754; color: white; }
        .bg-warning { background-color: #ffc107; color: black; }
        .bg-danger { background-color: #dc3545; color: white; }
    </style>
</head>
<body>

<h2>Quản lý đơn hàng</h2>

<div class="actions">
    <form method="get" action="order-manager" style="display: inline;">
        <input type="text" name="search" placeholder="Tìm theo tên khách hàng..." value="${param.search}">
        <button type="submit">Tìm</button>
    </form>
</div>

<table>
    <thead>
        <tr>
            <th>Mã ĐH</th>
            <th>Tên KH</th>
            <th>Ngày đặt</th>
            <th>Tổng tiền</th>
            <th>PTTT</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody>
    <%
        List<Order> orderList = (List<Order>) request.getAttribute("orderList");
        
        // Khởi tạo các đối tượng định dạng ở đây để tái sử dụng
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy ");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        if (orderList != null && !orderList.isEmpty()) {
            for (Order order : orderList) {
    %>
    <tr>
        <td>#<%= order.getId() %></td>
        <td><%= order.getCustomerUsername()%></td>
        <td><%= dateFormat.format(order.getOrderDate()) %></td>
        <td><%= currencyFormat.format(order.getTotalAmount()) %></td>
        <td><%= order.getPaymentMethod() %></td>
        <td>
             <%
                String status = order.getStatus();
                String badgeClass = "bg-secondary"; // Mặc định
                if ("Đã giao".equals(status)) { badgeClass = "bg-success"; } 
                else if ("Đang xử lý".equals(status)) { badgeClass = "bg-warning"; } 
                else if ("Đã hủy".equals(status)) { badgeClass = "bg-danger"; }
            %>
            <span class="badge <%= badgeClass %>"><%= status %></span>
        </td>
        <td>
            <button>Xem chi tiết</button>
            <button>Cập nhật</button>
        </td>
    </tr>
    <%
            } // Kết thúc for
        } else {
    %>
        <tr>
            <%-- Dùng colspan="8" để thông báo chiếm hết 8 cột của bảng --%>
            <td colspan="8" style="text-align: center; padding: 20px; font-style: italic;">Không có đơn hàng nào.</td>
        </tr>
    <%
        } // Kết thúc if-else
    %>
</tbody>
</table>

<%-- Các modal cho việc xem chi tiết, cập nhật trạng thái có thể được thêm ở đây --%>

</body>
</html>