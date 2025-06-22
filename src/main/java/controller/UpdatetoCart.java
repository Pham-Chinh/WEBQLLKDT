/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;

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
        Cart cart = (Cart)session.getAttribute("cart");
        if (cart != null && !cart.isEmpty()) {
            try {
                // Bước 3: Lấy ID và số lượng mới từ request.
                // Dữ liệu từ request luôn luôn là kiểu String.
                String productIdStr = request.getParameter("id");
                String quantityStr = request.getParameter("qty");

                // Bước 4: Ép kiểu dữ liệu String sang int để xử lý.
                // Đây là bước cực kỳ quan trọng để khớp với kiểu dữ liệu trong Cart.java.
                int productId = Integer.parseInt(productIdStr);
                int newQuantity = Integer.parseInt(quantityStr);

                // Bước 5: Gọi phương thức trong lớp Cart để cập nhật.
                // Phương thức này chúng ta đã sửa trong Cart.java.
                cart.updateItemQuantity(productId, newQuantity);
                
                // Bước 6: LƯU LẠI giỏ hàng đã thay đổi vào session.
                // Nếu thiếu dòng này, mọi thay đổi sẽ mất hết!
                session.setAttribute("cart", cart);

            } catch (NumberFormatException e) {
                // Bắt lỗi nếu id hoặc qty không phải là số hợp lệ.
                System.out.println("Lỗi ép kiểu tham số: " + e.getMessage());
                // Không làm gì cả và chỉ chuyển hướng về giỏ hàng.
            }
        }
        
        // Bước 7: Chuyển hướng người dùng trở lại trang giỏ hàng để thấy sự thay đổi.
        response.sendRedirect("cart.jsp");
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
