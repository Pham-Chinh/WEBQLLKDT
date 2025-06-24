/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.taikhoan;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CancelOrderServlet", urlPatterns = {"/cancel-order"})
public class CancelOrderServlet extends HttpServlet {

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
            out.println("<title>Servlet CancelOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CancelOrderServlet at " + request.getContextPath() + "</h1>");
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

        // Bước 1: KIỂM TRA XEM NGƯỜI DÙNG ĐÃ ĐĂNG NHẬP CHƯA
        taikhoan account = (taikhoan) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int userId = account.getId();

            // Bước 2: KIỂM TRA BẢO MẬT - ĐẢM BẢO ĐƠN HÀNG NÀY ĐÚNG LÀ CỦA USER NÀY
            Order order = OrderDAO.getOrderById(orderId);
            if (order == null || order.getUserID()!= userId) {
                // Nếu đơn hàng không tồn tại, hoặc không thuộc về user này -> không cho phép hủy
                session.setAttribute("message", "Lỗi: Bạn không có quyền hủy đơn hàng này.");
                response.sendRedirect("my-orders");
                return;
            }

            // Bước 3: KIỂM TRA LOGIC - CHỈ CHO PHÉP HỦY KHI ĐANG "ĐANG XỬ LÝ"
            if ("Đang xử lý".equalsIgnoreCase(order.getStatus())) {
                OrderDAO.updateOrderStatus(orderId, "Đã hủy");
                session.setAttribute("message", "Đã hủy thành công đơn hàng #" + orderId);
            } else {
                session.setAttribute("message", "Không thể hủy đơn hàng #" + orderId + " vì đã được xử lý.");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("message", "Lỗi: ID đơn hàng không hợp lệ.");
        }
        
        // Bước 4: Chuyển hướng người dùng trở lại trang lịch sử đơn hàng
        response.sendRedirect("my-orders");
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
