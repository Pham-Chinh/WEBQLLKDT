<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Cart, model.CartItem, java.util.*, java.text.NumberFormat, java.util.Locale" %>

<%
    // Chỉ lấy giỏ hàng chính, không có "Mua ngay"
    Cart cart = (Cart) session.getAttribute("cart");

    // Nếu không có giỏ hàng hoặc giỏ hàng trống, chuyển về trang giỏ hàng
    if (cart == null || cart.isEmpty()) {
        response.sendRedirect("cart.jsp");
        return; // Dừng chạy code JSP ở đây
    }
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
%>
<html>
<head>
    <title>Thanh toán</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
        :root {
            --primary-color: #ffc107; --primary-hover-color: #e0a800; --dark-color: #212529;
            --text-color: #495057; --border-color: #dee2e6; --light-gray-bg: #f8f9fa;
        }
        
        body { font-family: 'Roboto', sans-serif; background-color: var(--light-gray-bg); color: var(--text-color); padding: 20px; box-sizing: border-box; }
        .checkout-container { max-width: 1100px; margin: auto; display: flex; gap: 30px; align-items: flex-start; }
        .customer-info { flex: 2; background: #fff; padding: 30px; border-radius: 8px; }
        .order-summary { flex: 1; background: #fff; padding: 30px; border-radius: 8px; position: sticky; top: 20px; }
        h2, h3 { font-weight: 700; color: var(--dark-color); }
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; font-weight: 500; margin-bottom: 8px; }
        .form-control { width: 100%; padding: 12px; border: 1px solid var(--border-color); border-radius: 5px; font-size: 16px; }
        .order-item { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid var(--border-color); }
        .order-item:last-child { border-bottom: none; }
        .total-row { display: flex; justify-content: space-between; font-size: 18px; font-weight: 700; margin-top: 20px; padding-top: 20px; border-top: 2px solid var(--dark-color); color: var(--dark-color); }
        .place-order-btn { display: block; width: 100%; padding: 15px; background-color: var(--primary-color); color: var(--dark-color); text-align: center; border: none; border-radius: 5px; font-size: 16px; font-weight: 700; cursor: pointer; text-decoration: none; transition: background-color 0.2s ease-in-out; margin-top: 20px; }
        .place-order-btn:hover { background-color: var(--primary-hover-color); }
        .payment-method-options { margin-top: 15px; border: 1px solid var(--border-color); border-radius: 5px; }
        .payment-option { padding: 15px; cursor: pointer; transition: background-color 0.2s; border-bottom: 1px solid var(--border-color); }
        .payment-option:last-child { border-bottom: none; }
        .payment-option:hover { background-color: var(--light-gray-bg); }
        .payment-option label { display: flex; align-items: center; width: 100%; cursor: pointer; font-weight: 500 !important; margin-bottom: 0 !important; }
        .payment-option input[type="radio"] { margin-right: 15px; transform: scale(1.2); }
        .payment-option label i { font-size: 20px; width: 24px; text-align: center; margin-right: 10px; color: var(--dark-color); }
        .payment-option label img { margin-right: 10px; }
    </style>
</head>
<body>
    <div class="checkout-container">
        <div class="customer-info">
            <h2>Thông tin giao hàng</h2>
            <p>Điền thông tin của bạn để chúng tôi giao hàng nhé.</p>
            <form id="customer-info-form" action="PlaceOrderServlet" method="post">
                <div class="form-group"><label for="name">Họ và Tên</label><input type="text" id="name" name="name" class="form-control" required></div>
                <div class="form-group"><label for="phone">Số điện thoại</label><input type="tel" id="phone" name="phone" class="form-control" required></div>
                <div class="form-group"><label for="address">Địa chỉ nhận hàng</label><input type="text" id="address" name="address" class="form-control" required></div>
                <div class="form-group"><label for="email">Email (tùy chọn)</label><input type="email" id="email" name="email" class="form-control"></div>
                <div class="form-group"><label for="notes">Ghi chú (tùy chọn)</label><textarea id="notes" name="notes" class="form-control" rows="3"></textarea></div>

                <%-- ✅ KHỐI THANH TOÁN ĐÃ ĐƯỢC KHÔI PHỤC ĐẦY ĐỦ --%>
                <div class="form-group mt-4">
                    <h3>Hình thức thanh toán</h3>
                    <div class="payment-method-options">
                        <div class="payment-option">
                            <label for="payment_cod">
                                <input type="radio" id="payment_cod" name="paymentMethod" value="Thanh toán tiền mặt khi nhận hàng" checked>
                                <i class="fa-solid fa-money-bill-wave"></i>
                                <span>Thanh toán tiền mặt khi nhận hàng</span>
                            </label>
                        </div>
                        <div class="payment-option">
                             <label for="payment_bank">
                                <input type="radio" id="payment_bank" name="paymentMethod" value="Chuyển khoản ngân hàng">
                                <i class="fa-solid fa-building-columns"></i>
                                <span>Chuyển khoản ngân hàng</span>
                            </label>
                        </div>
                        <div class="payment-option">
                            <label for="payment_momo">
                                <input type="radio" id="payment_momo" name="paymentMethod" value="Ví Momo">
                                <img src="https://upload.wikimedia.org/wikipedia/vi/f/fe/MoMo_Logo.png" alt="Momo" width="24">
                                <span>Ví Momo</span>
                            </label>
                        </div>
                         <div class="payment-option">
                            <label for="payment_vnpay">
                                 <input type="radio" id="payment_vnpay" name="paymentMethod" value="Qua VNPAY-QR">
                                <img src="https://vnpay.vn/s1/statics.vnpay.vn/media/logo/logo-vnpay-qr-h.png" alt="VNPAY" height="24">
                                <span>Qua VNPAY-QR</span>
                            </label>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <div class="order-summary">
            <h3>Đơn hàng của bạn</h3>
            <% for (CartItem item : cart.getItems()) { %>
                <div class="order-item">
                    <span><%= item.getProduct().getName() %> x <strong><%= item.getQuantity() %></strong></span>
                    <span><%= currencyFormat.format(item.getTotalPrice()) %></span>
                </div>
            <% } %>
            <div class="total-row">
                <span>TỔNG CỘNG</span>
                <span><%= currencyFormat.format(cart.getTotalAmount()) %></span>
            </div>
            <button type="submit" form="customer-info-form" class="place-order-btn">ĐẶT HÀNG</button>
        </div>
    </div>
</body>
</html>