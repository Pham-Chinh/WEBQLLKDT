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
import model.Cart;
import model.taikhoan;

/**
 *
 * @author Admin
 */
@WebServlet(name = "PlaceOrderServlet", urlPatterns = {"/PlaceOrderServlet"})
public class PlaceOrderServlet extends HttpServlet {

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
            out.println("<title>Servlet PlaceOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PlaceOrderServlet at " + request.getContextPath() + "</h1>");
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
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        // ✅ CHỈ LẤY GIỎ HÀNG CHÍNH
        Cart cart = (Cart) session.getAttribute("cart");
        taikhoan account = (taikhoan)session.getAttribute("account");
        Integer userID = null;
        if(account != null){
            userID = account.getId();
        }

        // Lấy thông tin khách hàng từ form
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String notes = request.getParameter("notes");
        String paymentMethod = request.getParameter("paymentMethod");

        if (cart != null && !cart.isEmpty() && name != null && !name.isEmpty()) {
            OrderDAO orderDAO = new OrderDAO();
            int orderId = orderDAO.saveOrder(cart, name, phone, address, notes, paymentMethod,userID);

            if (orderId > 0) {
                // Nếu lưu đơn hàng thành công
                // ✅ CHỈ CẦN XÓA GIỎ HÀNG CHÍNH
                session.removeAttribute("cart");
                
                request.setAttribute("orderId", orderId);
                request.getRequestDispatcher("thankyou.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Đã có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("cart.jsp");
        }
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
