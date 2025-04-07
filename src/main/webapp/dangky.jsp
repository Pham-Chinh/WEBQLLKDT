<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký tài khoản</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #cddc39; /* nền vàng olive */
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .register-box {
            background: white;
            padding: 30px;
            border-radius: 10px;
            width: 400px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
        }

        .register-box h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .register-box input[type="text"],
        .register-box input[type="password"] {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        .register-box button {
            width: 100%;
            padding: 12px;
            background-color: #3e3e3e;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        .register-box .back-link {
            display: block;
            text-align: center;
            margin-top: 15px;
            text-decoration: none;
            color: #3e3e3e;
            font-size: 14px;
        }

        .register-box .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="register-box">
    <h2>Đăng ký tài khoản</h2>
    <form action="dangky" method="post">
        <input type="text" name="username" placeholder="Tên người dùng" required>
        <input type="password" name="password" placeholder="Mật khẩu" required>
        <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>
        <button type="submit">Đăng ký</button>
    </form>
    <a href="login.jsp" class="back-link">← Quay lại trang đăng nhập</a>
</div>
</body>
</html>
