package controller;

import dao.productDAO;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.product;

@WebServlet(name = "manager-product", urlPatterns = {"/manager-product"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 15
)
public class productServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("search");
        if (keyword == null) keyword = "";

        List<product> list = productDAO.getAll(keyword);
        request.setAttribute("listProduct", list);

        // ✅ Trả về đúng nội dung của phần sản phẩm để load bằng fetch
        request.getRequestDispatcher("manager-product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            productDAO.delete(id);

            // ✅ Trả lại danh sách sau khi xóa
            List<product> list = productDAO.getAll("");
            request.setAttribute("listProduct", list);
            request.getRequestDispatcher("manager-product.jsp").forward(request, response);
            return;
        }

        // ✅ Thông tin sản phẩm
        int id = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0;
        String name = request.getParameter("name");
        String desc = request.getParameter("description");
        String priceParam = request.getParameter("price");
        String label = request.getParameter("label");
        double price = 0;

        if (priceParam != null && !priceParam.trim().isEmpty()) {
            price = Double.parseDouble(priceParam);
        }

        // ✅ Xử lý ảnh sản phẩm
        Part filePart = request.getPart("image");
        byte[] imageBytes = null;
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream input = filePart.getInputStream()) {
                imageBytes = input.readAllBytes();
            }
        }

        if ("add".equals(action)) {
            product p = new product(0, name, desc, price, imageBytes, label);
            productDAO.add(p);
        } else if ("edit".equals(action)) {
            product existing = productDAO.getProductById(id);
            if (imageBytes == null && existing != null) {
                imageBytes = existing.getImage();
            }
            product p = new product(id, name, desc, price, imageBytes, label);
            productDAO.update(p);
        }

        String keyword = request.getParameter("search");
        List<product> list = productDAO.getAll(keyword != null ? keyword : "");
        request.setAttribute("listProduct", list);
        request.setAttribute("listProduct", list);
        request.getRequestDispatcher("manager-product.jsp").forward(request, response);
    }
}
