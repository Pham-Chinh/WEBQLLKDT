<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Order, java.text.NumberFormat, java.util.Locale, java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đơn hàng của tôi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="background-color: #f8f9fa;">

    <jsp:include page="header.jsp" />

    <div class="container my-5">
        <h1 class="mb-4">Lịch sử mua hàng</h1>

        <div class="card shadow-sm">
            <div class="card-body">
                <%
                    List<Order> myOrderList = (List<Order>) request.getAttribute("myOrderList");
                    if (myOrderList != null && !myOrderList.isEmpty()) {
                %>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Mã Đơn Hàng</th>
                                <th>Ngày Đặt</th>
                                <th>Tổng Tiền</th>
                                <th>Trạng Thái</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                for (Order order : myOrderList) { 
                            %>
                                <tr>
                                    <td><strong>#<%= order.getId() %></strong></td>
                                    <td><%= dateFormat.format(order.getOrderDate()) %></td>
                                    <td><strong class="text-danger"><%= currencyFormat.format(order.getTotalAmount()) %></strong></td>
                                    <td><span class="badge rounded-pill bg-success"><%= order.getStatus() %></span></td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-outline-primary" onclick="viewOrderDetails('<%= order.getId() %>')">
                                            Xem chi tiết
                                        </button>
                                         <% if ("Đang xử lý".equalsIgnoreCase(order.getStatus())) { %>
                                            <form action="cancel-order" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này? Hành động này không thể hoàn tác.');">
                                                <input type="hidden" name="orderId" value="<%= order.getId() %>">
                                                <button type="submit" class="btn btn-sm btn-danger">Hủy đơn</button>
                                            </form>
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                    <div class="text-center p-5">
                        <p class="lead">Bạn chưa có đơn hàng nào.</p>
                        <a href="index.jsp" class="btn btn-primary">Bắt đầu mua sắm</a>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <div class="modal fade" id="orderDetailModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header"><h5 class="modal-title">Chi tiết Đơn hàng #<span id="modalOrderId"></span></h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
                <div class="modal-body" id="modalBodyContent"></div>
                <div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button></div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    
</body>
</html>