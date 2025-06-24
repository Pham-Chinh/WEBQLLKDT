/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CartDAO;
import dao.productDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.CartItem;
import model.product;
import model.taikhoan;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AddtoCart", urlPatterns = {"/AddtoCart"})
public class AddtoCart extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddtoCart</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddtoCart at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        processRequest(request, response);
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // ✅ Lấy session ngay từ đầu để dùng chung
    HttpSession session = request.getSession();

    try {
        String productIdStr = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        
        // ✅ Kiểm tra xem người dùng đã đăng nhập chưa
        taikhoan account = (taikhoan) session.getAttribute("account");
        
        if (productIdStr == null || quantityStr == null) {
            throw new IllegalArgumentException("Thiếu productId hoặc quantity");
        }
        
        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        // ✅ BẮT ĐẦU LOGIC MỚI: Rẽ nhánh xử lý dựa trên trạng thái đăng nhập
        if (account != null) {
            // ---- TRƯỜNG HỢP 1: NGƯỜI DÙNG ĐÃ ĐĂNG NHẬP ----
            // Lấy ID của người dùng
            int userId = account.getId();
            
            // Gọi CartDAO để lưu/cập nhật giỏ hàng vào DATABASE
            CartDAO.addToCart(userId, productId, quantity);
            
            // (Tùy chọn) Gửi thông báo thành công
            session.setAttribute("message", "Đã thêm sản phẩm vào giỏ hàng của bạn!");

        } else {
            // ---- TRƯỜNG HỢP 2: LÀ KHÁCH (CHƯA ĐĂNG NHẬP) ----
            // Giữ nguyên logic cũ của bạn: lưu vào SESSION
            product p = productDAO.getProductById(productId);
            if (p == null) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm với ID " + productId);
            }
            
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }
            cart.addItem(new CartItem(p, quantity));
        }
        // ✅ KẾT THÚC LOGIC MỚI

        // ✅ COMMENT OUT CÁC DÒNG GỬI JSON RESPONSE VÌ NÓ XUNG ĐỘT VỚI sendRedirect
        // Khi dùng sendRedirect, chúng ta không cần gửi JSON nữa.
        /*
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"success\", \"message\": \"Đã thêm sản phẩm!\"}");
        */

    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        // ✅ COMMENT OUT CÁC DÒNG GỬI JSON RESPONSE
        /*
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        */
    }
    
    // ✅ Luôn chuyển hướng về trang giỏ hàng sau khi xử lý xong
    response.sendRedirect("cart.jsp");
}

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
