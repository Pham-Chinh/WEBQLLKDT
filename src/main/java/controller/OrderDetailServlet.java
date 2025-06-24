/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dao.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CartItem;
import model.Order;
import model.taikhoan;

/**
 *
 * @author Admin
 */
@WebServlet(name = "OrderDetailServlet", urlPatterns = {"/order-details"})
public class OrderDetailServlet extends HttpServlet {

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
            out.println("<title>Servlet OrderDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderDetailServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsonResponse;
        
        HttpSession session = request.getSession();
        
        // ✅ BƯỚC BẢO MẬT 1: KIỂM TRA XEM NGƯỜI DÙNG ĐÃ ĐĂNG NHẬP CHƯA
        taikhoan loggedInAccount = (taikhoan) session.getAttribute("account");
        if (loggedInAccount == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Lỗi 401: Chưa xác thực
            jsonResponse = "{\"error\": \"Vui lòng đăng nhập để xem chi tiết đơn hàng.\"}";
            out.print(jsonResponse);
            out.flush();
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int userId = loggedInAccount.getId();
            
            Order orderInfo = OrderDAO.getOrderById(orderId);
            
            // ✅ BƯỚC BẢO MẬT 2: KIỂM TRA XEM ĐƠN HÀNG CÓ THUỘC VỀ NGƯỜI DÙNG NÀY KHÔNG
            if (orderInfo == null || orderInfo.getUserID()!= userId) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Lỗi 403: Không có quyền truy cập
                jsonResponse = "{\"error\": \"Bạn không có quyền xem đơn hàng này.\"}";
            } else {
                // Nếu tất cả kiểm tra đều qua, lấy chi tiết sản phẩm
                List<CartItem> items = OrderDAO.getOrderDetailsById(orderId);
                
                Map<String, Object> data = new HashMap<>();
                data.put("orderInfo", orderInfo);
                data.put("orderItems", items);

                jsonResponse = new Gson().toJson(data);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse = "{\"error\": \"Order ID không hợp lệ.\"}";
        }
        
        out.print(jsonResponse);
        out.flush();
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
        processRequest(request, response);
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
