/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
         HttpSession session = request.getSession();
    try {
        String productIdStr = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");

        if (productIdStr == null || quantityStr == null) {
            throw new IllegalArgumentException("Thiếu productId hoặc quantity");
        }

        
        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        product p = productDAO.getProductById(productId);
        if (p == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm với ID " + productId);
        }

//        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        cart.addItem(new CartItem(p, quantity));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"success\", \"message\": \"Đã thêm sản phẩm!\"}");

    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
    }
    
    session.setAttribute("message", "Đã thêm sản phẩm vào giỏ hàng");
    response.sendRedirect("cart.jsp");
}

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
