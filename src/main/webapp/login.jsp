<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background: #EEEE00 center center no-repeat;
            background-size: cover;
        }

        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100vw;
            height: 100vh;
            background: rgba(0, 0, 0, 0.4);
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-modal {
            background: white;
            padding: 30px;
            border-radius: 10px;
            width: 360px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
            position: relative;
        }

        .login-modal h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .login-modal input[type="text"],
        .login-modal input[type="password"] {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 6px;
        }

        .login-modal button {
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

        .login-modal .forgot {
            display: block;
            text-align: center;
            margin-top: 10px;
            color: #1877f2;
            text-decoration: none;
        }

        .login-modal .or {
            text-align: center;
            margin: 20px 0;
            color: #888;
        }

        .login-modal .create-btn {
            background-color: #42b72a;
        }

        .close-btn {
            position: absolute;
            top: 10px;
            right: 14px;
            font-size: 20px;
            font-weight: bold;
            color: #555;
            cursor: pointer;
        }

        .error-message {
            color: red;
            text-align: center;
            font-size: 14px;
            margin-top: -10px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="modal-overlay">
        <div class="login-modal">
            <span class="close-btn" onclick="window.location='index.jsp'">&times;</span>
            <h2>Đăng nhập</h2>

            <% 
                String error = request.getParameter("error");
                if ("1".equals(error)) {
            %>
                <div class="error-message">Tài khoản hoặc mật khẩu không đúng!</div>
            <% } %>
            <%
    String reg = request.getParameter("reg");
    if ("1".equals(reg)) {
%>
    <div class="success-message">Đăng ký thành công! Mời bạn đăng nhập.</div>
<% } %>


            <form action="${pageContext.request.contextPath}/login" method="post">
                <input type="text" name="username" placeholder="Tên tài khoản" required>
                <input type="password" name="password" placeholder="Mật khẩu" required>
                <button type="submit">Đăng nhập</button>
                <a href="#" class="forgot">Quên mật khẩu?</a>
                <div class="or">hoặc</div>
                <button type="button" class="create-btn" onclick="window.location='dangky.jsp'">Tạo tài khoản mới</button>
            </form>
        </div>
    </div>
</body>
</html>
