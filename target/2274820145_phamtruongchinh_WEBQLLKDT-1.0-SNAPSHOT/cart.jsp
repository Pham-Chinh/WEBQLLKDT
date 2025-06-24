<%@page import="dao.CartDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.*, java.util.*, java.text.NumberFormat, java.util.Locale" %>

<%
    // ✅ LOGIC MỚI ĐỂ LẤY GIỎ HÀNG
    Cart cart = null;
    taikhoan loggedInAccount = (taikhoan) session.getAttribute("account");

    if (loggedInAccount != null) {
        // Nếu đã đăng nhập, lấy giỏ hàng từ DATABASE
        int userId = loggedInAccount.getId();
        cart = CartDAO.getCartByUserId(userId);
    } else {
        // Nếu là khách, lấy giỏ hàng từ SESSION
        cart = (Cart) session.getAttribute("cart");
    }

    // Nếu sau tất cả các bước mà giỏ hàng vẫn là null, hãy tạo một giỏ hàng mới
    if (cart == null) {
        cart = new Cart();
    }
    
    // Lưu lại giỏ hàng vào session (quan trọng cho các servlet khác và để hiển thị huy hiệu)
    session.setAttribute("cart", cart);

    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
%>

<%-- Phần còn lại của file cart.jsp giữ nguyên --%>
...

<html>
<head>
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
        /* === BẮT ĐẦU CSS ĐÃ THIẾT KẾ LẠI === */
        :root {
            --primary-color: #ffc107; /* Màu vàng chủ đạo từ trang chủ của bạn */
            --primary-hover-color: #e0a800; /* Màu vàng đậm hơn khi di chuột vào */
            --dark-color: #212529;      /* Màu đen/xám đậm cho tiêu đề */
            --text-color: #495057;      /* Màu xám cho chữ thường, dễ đọc hơn */
            --border-color: #dee2e6;    /* Màu viền/kẻ */
            --light-gray-bg: #f8f9fa;   /* Màu nền xám rất nhạt */
            --success-bg: #d1e7dd;      /* Màu nền cho thông báo thành công */
            --success-text: #0f5132;    /* Màu chữ cho thông báo thành công */
            --danger-color: #dc3545;    /* Màu đỏ cho nút xóa */
        }

        

        body {
            font-family: 'Roboto', sans-serif;
            background-color: #fff;
            color: var(--text-color);
            padding: 20px;
            box-sizing: border-box;
        }

        .cart-container {
            max-width: 960px;
            margin: auto;
            background: #fff;
            padding: 30px;
        }

        h2 {
            font-weight: 700;
            margin-bottom: 25px;
            color: var(--dark-color);
            font-size: 28px;
        }
        
        /* CSS cho thông báo đơn giản */
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: 500;
            border: 1px solid transparent;
        }
        .alert.success {
            background-color: var(--success-bg);
            color: var(--success-text);
            border-color: #badbcc;
        }

        .cart-table {
            width: 100%;
            border-collapse: collapse;
        }

        .cart-table th, .cart-table td {
            padding: 15px;
            text-align: left;
            vertical-align: middle;
            border-bottom: 1px solid var(--border-color);
        }

        .cart-table thead {
            background-color: var(--light-gray-bg);
        }

        .cart-table th {
            font-weight: 500;
            font-size: 14px;
            color: var(--dark-color);
            text-transform: uppercase;
        }

        .cart-table tbody tr:last-child {
            border-bottom: none;
        }

        .product-info { display: flex; align-items: center; }
        .product-info img { width: 80px; height: 80px; object-fit: cover; border-radius: 8px; margin-right: 15px; border: 1px solid var(--border-color); }
        .product-info .product-name { font-weight: 500; color: var(--dark-color); }

        .quantity-selector { display: flex; align-items: center; }
        .quantity-selector form { margin: 0; }
        .quantity-selector .qty-btn { width: 32px; height: 32px; border: 1px solid var(--border-color); background-color: #fff; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: background-color 0.2s; }
        .quantity-selector .qty-btn:hover { background-color: var(--light-gray-bg); }
        .quantity-selector .qty-btn:disabled { background-color: #e9ecef; cursor: not-allowed; opacity: 0.7; }
        .quantity-selector .qty-input { width: 45px; height: 32px; text-align: center; font-weight: 500; color: var(--dark-color); border: 1px solid var(--border-color); border-left: none; border-right: none; -moz-appearance: textfield; }
        .quantity-selector .qty-input::-webkit-outer-spin-button, .quantity-selector .qty-input::-webkit-inner-spin-button { -webkit-appearance: none; margin: 0; }
        
        .delete-btn { background: none; border: none; cursor: pointer; font-size: 18px; color: #888; transition: color 0.2s; }
        .delete-btn:hover { color: var(--danger-color); }

        .cart-summary { margin-top: 30px; float: right; width: 100%; max-width: 350px; }
        .total-row { display: flex; justify-content: space-between; font-size: 18px; font-weight: 700; margin-bottom: 20px; color: var(--dark-color); }

        .checkout-btn {
            display: block;
            width: 100%;
            padding: 15px;
            background-color: var(--primary-color);
            color: var(--dark-color);
            text-align: center;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            text-decoration: none;
            transition: background-color 0.2s ease-in-out;
        }
        .checkout-btn:hover {
            background-color: var(--primary-hover-color);
        }
        /* Thêm vào trong thẻ <style> */
    .btn-continue-shopping {
        display: inline-block; /* Giúp thẻ <a> có thể nhận padding, margin */
        padding: 12px 25px;
        font-size: 16px;
        font-weight: 700;
        text-align: center;
        text-decoration: none; /* Bỏ gạch chân của liên kết */
        cursor: pointer;

        color: var(--dark-color); /* Dùng biến màu ta đã định nghĩa */
        background-color: var(--primary-color); /* Màu vàng chủ đạo */

        border: none;
        border-radius: 5px; /* Bo góc giống nút Thanh toán */
        transition: background-color 0.2s ease-in-out;
    }

    .btn-continue-shopping:hover {
        background-color: var(--primary-hover-color); /* Màu vàng đậm hơn khi di chuột */
    }
        /* === KẾT THÚC CSS ĐÃ THIẾT KẾ LẠI === */
    </style>
</head>
<body>

<div class="cart-container">
    <h2>Giỏ hàng của bạn</h2>
    
    <%-- Hiển thị thông báo thành công (nếu có) một cách đơn giản --%>
    <%
        String message = (String) session.getAttribute("message");
        if (message != null) {
    %>
        <div class="alert success">
            <%= message %>
        </div>
    <%
            session.removeAttribute("message");
        }
    %>

    <% if (cart.isEmpty()) { %>
        <p>Giỏ hàng của bạn hiện đang trống.</p>
        <br>
        <a href="index.jsp" class="btn-continue-shopping">Tiếp tục mua sắm</a>
    <% } else { %>
        <table class="cart-table">
            <thead>
                <tr>
                    <th style="width: 45%;">Sản phẩm</th>
                    <th style="width: 15%;">Đơn giá</th>
                    <th style="width: 20%;">Số lượng</th>
                    <th style="width: 15%;">Thành tiền</th>
                    <th style="width: 5%;"></th>
                </tr>
            </thead>
            <tbody>
                <% for (CartItem item : cart.getItems()) {
                    product p = item.getProduct();
                    int currentQuantity = item.getQuantity();
                %>
                <tr>
                    <td>
                        <div class="product-info">
                           <img src="ImageServlet?id=<%= p.getId() %>" alt="<%= p.getName() %>">
                            <span class="product-name"><%= p.getName() %></span>
                        </div>
                    </td>
                    <td><%= currencyFormat.format(p.getPrice()) %></td>
                    <td>
                        <%-- GIỮ NGUYÊN LOGIC GỐC: Dùng form submit và tải lại trang --%>
                        <div class="quantity-selector">
                            <form action="UpdatetoCart" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= p.getId() %>">
                                <input type="hidden" name="qty" value="<%= currentQuantity - 1 %>">
                                <button type="submit" class="qty-btn" <%= (currentQuantity <= 1) ? "disabled" : "" %>>-</button>
                            </form>
                            <input type="number" class="qty-input" value="<%= currentQuantity %>" readonly>
                            <form action="UpdatetoCart" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= p.getId() %>">
                                <input type="hidden" name="qty" value="<%= currentQuantity + 1 %>">
                                <button type="submit" class="qty-btn">+</button>
                            </form>
                        </div>
                    </td>
                    <td><strong><%= currencyFormat.format(item.getTotalPrice()) %></strong></td>
                    <td>
                        <form action="RemoveCartServlet" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');">
                            <input type="hidden" name="id" value="<%= p.getId() %>">
                            <button type="submit" class="delete-btn" title="Xóa sản phẩm">
                                <i class="fa-solid fa-trash-can"></i>
                            </button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <div class="cart-summary">
            <div class="total-row">
                <span>Tổng tiền:</span>
                <span><%= currencyFormat.format(cart.getTotalAmount()) %></span>
            </div>
            <a href="checkout.jsp" class="checkout-btn">THANH TOÁN</a>
        </div>
        <div style="clear: both;"></div>
    <% } %>
</div>

</body>
</html>