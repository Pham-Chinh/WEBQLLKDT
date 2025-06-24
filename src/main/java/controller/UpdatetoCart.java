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
@WebServlet(name = "UpdatetoCart", urlPatterns = {"/UpdatetoCart"})
public class UpdatetoCart extends HttpServlet {

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
            out.println("<title>Servlet UpdatetoCart</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdatetoCart at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
        int newQuantity = Integer.parseInt(request.getParameter("qty"));

        // ✅ BƯỚC QUAN TRỌNG: KIỂM TRA XEM AI ĐANG THAO TÁC
        taikhoan account = (taikhoan) session.getAttribute("account");

        if (account != null) {
            // ---- NẾU ĐÃ ĐĂNG NHẬP: GỌI DAO ĐỂ CẬP NHẬT DATABASE ----
            int userId = account.getId();
            
            // Dòng debug để xem servlet có đi đúng nhánh không
            System.out.println("DEBUG: Đang cập nhật DATABASE cho User ID: " + userId + ", Product ID: " + productId);
            
            CartDAO.updateQuantity(userId, productId, newQuantity);

        } else {
            // ---- NẾU LÀ KHÁCH: SỬA GIỎ HÀNG TRONG SESSION (LOGIC CŨ CỦA BẠN) ----
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                // Dòng debug để xem servlet có đi đúng nhánh không
                System.out.println("DEBUG: Đang cập nhật SESSION cho Guest.");
                
                cart.updateItemQuantity(productId, newQuantity);
                session.setAttribute("cart", cart); // Lưu lại giỏ hàng vào session
            }
        }
    } catch (NumberFormatException e) {
        System.out.println("Lỗi ép kiểu tham số: " + e.getMessage());
        e.printStackTrace();
    }
    
    // Luôn chuyển hướng về trang giỏ hàng sau khi xử lý xong
    response.sendRedirect("cart.jsp");
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
