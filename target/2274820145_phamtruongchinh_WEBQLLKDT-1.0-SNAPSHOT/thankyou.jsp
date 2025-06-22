<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt hàng thành công</title>
    <style>
         @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');

        :root {
            --primary-color: #ffc107;
            --primary-hover-color: #e0a800;
            --dark-color: #212529;
            --text-color: #495057;
            --success-color: #198754;
            --light-gray-bg: #f8f9fa;
        }
       
        body {
            font-family: 'Roboto', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: var(--light-gray-bg);
        }
        .thank-you-container {
            text-align: center;
            padding: 50px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px M15px rgba(0,0,0,0.1);
            max-width: 500px;
        }
        .checkmark {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: block;
            stroke-width: 3;
            stroke: white;
            stroke-miterlimit: 10;
            box-shadow: inset 0px 0px 0px var(--success-color);
            animation: fill .4s ease-in-out .4s forwards, scale .3s ease-in-out .9s both;
            margin: 0 auto 20px auto;
        }
        .checkmark__circle {
            stroke-dasharray: 166;
            stroke-dashoffset: 166;
            stroke-width: 3;
            stroke-miterlimit: 10;
            stroke: var(--success-color);
            fill: none;
            animation: stroke .6s cubic-bezier(0.65, 0, 0.45, 1) forwards;
        }
        .checkmark__check {
            transform-origin: 50% 50%;
            stroke-dasharray: 48;
            stroke-dashoffset: 48;
            animation: stroke .3s cubic-bezier(0.65, 0, 0.45, 1) .8s forwards;
        }
        h1 {
            color: var(--dark-color);
            font-weight: 700;
            margin-bottom: 15px;
        }
        p {
            font-size: 18px;
            color: var(--text-color);
            line-height: 1.6;
        }
        .order-id {
            font-weight: bold;
            color: var(--success-color);
        }
        .btn-home {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 30px;
            background-color: var(--primary-color);
            color: var(--dark-color);
            text-decoration: none;
            border-radius: 5px;
            font-weight: 700;
            transition: background-color 0.2s ease-in-out;
        }
        .btn-home:hover {
            background-color: var(--primary-hover-color);
        }
        
        @keyframes stroke {
            100% { stroke-dashoffset: 0; }
        }
        @keyframes scale {
            0%, 100% { transform: none; }
            50% { transform: scale3d(1.1, 1.1, 1); }
        }
        @keyframes fill {
            100% { box-shadow: inset 0px 0px 0px 50px var(--success-color); }
        }
    </style>
</head>
<body>
    <div class="thank-you-container">
        <svg class="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
            <circle class="checkmark__circle" cx="26" cy="26" r="25" fill="none"/>
            <path class="checkmark__check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
        </svg>

        <h1>Cảm ơn bạn đã đặt hàng!</h1>
        <p>
            Đơn hàng của bạn với mã số <strong class="order-id">#<%= request.getAttribute("orderId") %></strong> đã được ghi nhận.
        </p>
        <p>Chúng tôi sẽ liên hệ với bạn để xác nhận trong thời gian sớm nhất.</p>
        
        <a href="index.jsp" class="btn-home">Quay về trang chủ</a>
    </div>
</body>
</html>