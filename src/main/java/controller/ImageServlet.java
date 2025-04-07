package controller;

import dao.productDAO;
import java.io.IOException;
import java.io.OutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.product;

@WebServlet(name = "ImageServlet", urlPatterns = {"/ImageServlet"})
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        product p = productDAO.getProductById(id);
        byte[] image = p.getImage();

        if (image != null) {
            response.setContentType("image/jpeg");
            response.setContentLength(image.length);
            OutputStream out = response.getOutputStream();
            out.write(image);
            out.flush();
            out.close();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // gọi lại GET cho tiện
    }

    @Override
    public String getServletInfo() {
        return "Trả ảnh sản phẩm từ CSDL";
    }
}
