
package controller;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
@WebServlet(name = "dangky", urlPatterns = {"/dangky"})
public class resgisterServlet extends HttpServlet {

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
            out.println("<title>Servlet resgisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet resgisterServlet at " + request.getContextPath() + "</h1>");
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
      
        try(Connection conn = Accounts.getConnection()) {
            String checkquer = "SELECT * FROM accounts WHERE username=?";
            PreparedStatement checkSt = conn.prepareStatement(checkquer);
            checkSt.setString(1, username);
            ResultSet rs = checkSt.executeQuery();
            if(rs.next()){
                response.sendRedirect("login.jsp?reg=0");
                return;
            }
            String insertQuery = "INSERT INTO accounts(username, password, role) VALUES (?, ?, 'user')";
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            response.sendRedirect("login.jsp?reg=1");
        } catch (Exception e) {
             e.printStackTrace();
            response.getWriter().println("Lỗi khi đăng ký!");
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
