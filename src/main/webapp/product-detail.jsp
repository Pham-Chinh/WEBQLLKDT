<%@ page import="model.product, dao.productDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<%
    int id = -1;
    product p = null;
    try {
        id = Integer.parseInt(request.getParameter("id"));
        p = productDAO.getProductById(id);
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><%= (p != null ? p.getName() : "Không tìm thấy sản phẩm") %></title>
        <header>
      <div class="container-fluid">
        <div class="row py-3 border-bottom">
          
          <div class="col-sm-4 col-lg-2 text-center text-sm-start d-flex gap-3 justify-content-center justify-content-md-start">
            <div class="d-flex align-items-center my-3 my-sm-0">
              <a href="index.jsp">
                <img src="images/logoc.png" alt="logo" class="img-fluid">
              </a>
            </div>
            <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar"
              aria-controls="offcanvasNavbar">
              <svg width="24" height="24" viewBox="0 0 24 24"><use xlink:href="#menu"></use></svg>
            </button>
          </div>
          
          <div class="col-sm-6 offset-sm-2 offset-md-0 col-lg-4">
            <div class="search-bar row bg-light p-2 rounded-4">
              <div class="col-md-4 d-none d-md-block">
                <select class="form-select border-0 bg-transparent">
                  <option>Tất cả danh mục</option>
                  <option>Vi điều khiển</option>
                  <option>Cảm biến</option>
                  <option>Mạch tích hợp(IC)</option>
                </select>
              </div>
              <div class="col-11 col-md-7">
                <form id="search-form" class="text-center" action="index.jsp" method="post">
                  <input type="text" class="form-control border-0 bg-transparent" placeholder="Tìm kiếm sản phẩm">
                </form>
              </div>
              <div class="col-1">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" d="M21.71 20.29L18 16.61A9 9 0 1 0 16.61 18l3.68 3.68a1 1 0 0 0 1.42 0a1 1 0 0 0 0-1.39ZM11 18a7 7 0 1 1 7-7a7 7 0 0 1-7 7Z"/></svg>
              </div>
            </div>
          </div>

          <div class="col-lg-4">
            <ul class="navbar-nav list-unstyled d-flex flex-row gap-3 gap-lg-5 justify-content-center flex-wrap align-items-center mb-0 fw-bold text-uppercase text-dark">
              <li class="nav-item active">
                <a href="index.jsp" class="nav-link">Trang chủ</a>
              </li>
              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle pe-3" role="button" id="pages" data-bs-toggle="dropdown" aria-expanded="false">Các trang</a>
                <ul class="dropdown-menu border-0 p-3 rounded-0 shadow" aria-labelledby="pages">
                  <li><a href="index.jsp" class="dropdown-item">Giới thiệu về chúng tôi</a></li>
                  <li><a href="index.jsp" class="dropdown-item">Cửa hàng </a></li>
                  <li><a href="index.jsp" class="dropdown-item">Sản phẩm chi tiết </a></li>
                  <li><a href="index.jsp" class="dropdown-item">Giỏ hàng </a></li>
                  <li><a href="index.jsp" class="dropdown-item">Thanh toán </a></li>
                  <li><a href="index.jsp" class="dropdown-item">Tin tức </a></li>
                  <li><a href="index.jsp" class="dropdown-item">Bài viết chi tiết </a></li>
                  
                  <li><a href="index.jsp" class="dropdown-item">Kết nối </a></li>
                  
                  <li><a href="index.jsp" class="dropdown-item">Tài khoản của tôi </a></li>
                  
                </ul>
              </li>
            </ul>
          </div>
          
          <div class="col-sm-8 col-lg-2 d-flex gap-5 align-items-center justify-content-center justify-content-sm-end">
            <ul class="d-flex justify-content-end list-unstyled m-0">
              <li>
                <a href="login.jsp" class="p-2 mx-1">
                  <svg width="24" height="24"><use xlink:href="#user"></use></svg>
                </a>
              </li>
              <li>
                <a href="#" class="p-2 mx-1">
                  <svg width="24" height="24"><use xlink:href="#wishlist"></use></svg>
                </a>
              </li>
              <li>
                <a href="#" class="p-2 mx-1" data-bs-toggle="offcanvas" data-bs-target="#offcanvasCart" aria-controls="offcanvasCart">
                  <svg width="24" height="24"><use xlink:href="#shopping-bag"></use></svg>
                </a>
              </li>
            </ul>
          </div>

        </div>
      </div>
    </header>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 40px;
            background-color: #f9f9f9;
        }
        img {
            border: 1px solid #ccc;
            margin-top: 20px;
        }
        .container {
            background: white;
            padding: 30px;
            max-width: 800px;
            margin: auto;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .back-link {
            display: inline-block;
            margin-top: 30px;
            text-decoration: none;
            color: #007BFF;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
<%
    if (p != null) {
%>
    <h1><%= p.getName() %></h1>
    <img src="ImageServlet?id=<%= p.getId() %>" width="300" alt="<%= p.getName() %>"><br><br>

    <p><strong>Giá:</strong> <%= p.getPrice() %> VNĐ</p>
    <p><strong>Mô tả:</strong> <%= p.getDescription() %></p>
    <p><strong>Danh mục:</strong>
        <%
            String label = p.getLabel();
            switch (label) {
                case "moi":
                    out.print("Mới về"); break;
                case "phobien":
                    out.print("Phổ biến"); break;
                case "noibat":
                    out.print("Nổi bật"); break;
                default:
                    out.print("Khác"); break;
            }
        %>
    </p>
<%
    } else {
%>
    <p style="color:red;">❌ Không tìm thấy sản phẩm với ID này.</p>
<%
    }
%>

<a href="index.jsp" class="back-link">⬅ Quay lại trang chủ</a>
</div>
</body>
</html>
