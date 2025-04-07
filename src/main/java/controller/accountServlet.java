package controller;

import dao.accountDAO;
import jakarta.servlet.annotation.MultipartConfig; // Thêm dòng này
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import model.taikhoan;

@WebServlet(name = "manager-account", urlPatterns = {"/manager-account"})
@MultipartConfig // ✅ Thêm dòng này để xử lý multipart/form-data
public class accountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("search");
        if (keyword == null) keyword = "";

        List<taikhoan> list = accountDAO.getAllAccount(keyword);
        request.setAttribute("listAccount", list);

        // Trả về phần nội dung tài khoản
        request.getRequestDispatcher("manager-account.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            accountDAO.deleteAccount(id);

            List<taikhoan> list = accountDAO.getAllAccount("");
            request.setAttribute("listAccount", list);
            request.getRequestDispatcher("manager-account.jsp").forward(request, response);
            return;
        }

        int id = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        taikhoan acc = new taikhoan(id, username, password, role);

        if ("add".equals(action)) {
            accountDAO.addAccount(acc);
        } else if ("edit".equals(action)) {
            accountDAO.updateAccount(acc);
        }

        List<taikhoan> list = accountDAO.getAllAccount("");
        request.setAttribute("listAccount", list);
        request.getRequestDispatcher("manager-account.jsp").forward(request, response);
    }
}
