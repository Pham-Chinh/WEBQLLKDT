/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "order-manager", urlPatterns = {"/order-manager"})
public class OrderManagerServlet extends HttpServlet {

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
            out.println("<title>Servlet OrderManagerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderManagerServlet at " + request.getContextPath() + "</h1>");
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
// Set encoding để đọc được tiếng Việt từ URL
    request.setCharacterEncoding("UTF-8");
    
    // 1. Lấy từ khóa tìm kiếm từ thanh URL (ví dụ: ...?search=Nguyen Van A)
    String keyword = request.getParameter("search");
    if (keyword == null) {
        keyword = ""; // Nếu người dùng không tìm kiếm gì, đặt từ khóa là rỗng để lấy tất cả
    }

    // 2. Tạo đối tượng DAO
    OrderDAO orderDAO = new OrderDAO();

    // 3. Gọi phương thức TÌM KIẾM trong DAO, thay vì getAllOrders() cũ
    // Phương thức này sẽ trả về tất cả đơn hàng nếu keyword là rỗng
    List<Order> orderList = orderDAO.searchOrdersByCustomerName(keyword);
    
    // 4. Đặt kết quả tìm kiếm vào request để gửi cho trang JSP
    request.setAttribute("orderList", orderList);
    
    // (Nên có) Gửi lại từ khóa đã tìm để hiển thị lại trên ô tìm kiếm, giúp người dùng biết họ vừa tìm gì
    request.setAttribute("searchKeyword", keyword);

    // 5. Chuyển tiếp đến trang JSP để hiển thị kết quả
    request.getRequestDispatcher("order-manager.jsp").forward(request, response);
        
        
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
