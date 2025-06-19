<%@ page import="model.product, dao.productDAO, java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  String keyword = request.getParameter("keyword");
  List<product> products = productDAO.searchProductsByName(keyword);
%>

<b class="px-2 py-1 d-block border-bottom">Sản phẩm gợi ý</b>

<% for (product p : products) { %>
  <a href="product-detail.jsp?id=<%= p.getId() %>" class="d-flex p-2 text-decoration-none text-dark border-bottom">
    <img src="ImageServlet?id=<%= p.getId() %>" width="50" height="50" class="me-2" style="object-fit:cover;">
    <div>
      <div><%= p.getName() %></div>
      <div class="text-danger"><%= p.getPrice() %>₫</div>
    </div>
  </a>
<% } %>

<% if (!products.isEmpty()) { %>
 <a href="timkiemsp.jsp?keyword=<%= keyword %>" class="d-block text-center py-2 text-primary bg-light text-decoration-none">
  Hiển thị tất cả kết quả cho "<%= keyword %>"
</a>
<% } else { %>
  <div class="text-center py-2 text-muted bg-light">
    Không tìm thấy kết quả yêu cầu cho "<%= keyword %>"
  </div>
<% } %>
