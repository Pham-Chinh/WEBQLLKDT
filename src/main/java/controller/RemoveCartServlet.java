/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CartDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.taikhoan;

/**
 *
 * @author Admin
 */
@WebServlet(name = "RemoveCartServlet", urlPatterns = {"/RemoveCartServlet"})
public class RemoveCartServlet extends HttpServlet {

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
            out.println("<title>Servlet RemoveCartServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RemoveCartServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int productId = Integer.parseInt(request.getParameter("productId"));
//
//        HttpSession session = request.getSession();
//        Cart cart = (Cart) session.getAttribute("cart");
//        if (cart != null) {
//            cart.removeItem(productId);
//        }
//
//        response.sendRedirect("index.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

    try {
        int productId = Integer.parseInt(request.getParameter("id"));

        // ✅ BƯỚC QUAN TRỌNG: KIỂM TRA XEM AI ĐANG XÓA
        taikhoan account = (taikhoan) session.getAttribute("account");

        if (account != null) {
            // ---- TRƯỜNG HỢP 1: NGƯỜI DÙNG ĐÃ ĐĂNG NHẬP ----
            // => Xóa sản phẩm trong DATABASE
            
            int userId = account.getId();
            // Gọi phương thức trong CartDAO để xóa sản phẩm khỏi bảng UserCarts
            CartDAO.removeItem(userId, productId);

        } else {
            // ---- TRƯỜNG HỢP 2: LÀ KHÁCH (CHƯA ĐĂNG NHẬP) ----
            // => Xóa sản phẩm trong SESSION (đây là logic cũ của bạn)
            
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                cart.removeItem(productId);
                // Lưu lại giỏ hàng đã được cập nhật vào session
                session.setAttribute("cart", cart);
            }
        }
        
        // Sau khi xóa xong, đặt một thông báo thành công để trang cart.jsp có thể hiển thị
        session.setAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng.");

    } catch (NumberFormatException e) {
        System.out.println("Lỗi khi xóa sản phẩm, ID không hợp lệ: " + e.getMessage());
        e.printStackTrace();
    }

    // Luôn chuyển hướng về trang giỏ hàng sau khi xử lý
    response.sendRedirect("cart.jsp");
 }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
