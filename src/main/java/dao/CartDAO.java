package dao;

import controller.Accounts; // Import lớp Accounts của bạn để lấy kết nối
import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CartDAO {

    // --- CÁC PHƯƠNG THỨC ĐƯỢC VIẾT THEO ĐÚNG STYLE CỦA BẠN ---
    // 1. Tất cả các phương thức đều là public static.
    // 2. Mỗi phương thức đều lấy kết nối bằng `Accounts.getConnection()`.
    // 3. Sử dụng try-with-resources để đảm bảo kết nối được đóng.

    /**
     * Lấy giỏ hàng của một người dùng từ database.
     * @param userId ID của người dùng
     * @return Một đối tượng Cart chứa các sản phẩm của người dùng đó.
     */
    public static Cart getCartByUserId(int userId) {
        Cart cart = new Cart();
        // Câu lệnh JOIN để lấy thông tin sản phẩm cùng với số lượng trong giỏ hàng
        String sql = "SELECT p.*, uc.quantity as cart_quantity " +
                     "FROM UserCarts uc JOIN products p ON uc.product_id = p.id " +
                     "WHERE uc.user_id = ?";
        
        try (Connection conn = Accounts.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Tạo đối tượng product từ thông tin lấy được
                product p = new product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBytes("image"),
                    rs.getString("label"),
                    rs.getInt("quantity") // Đây là số lượng tồn kho của sản phẩm
                );
                // Lấy số lượng sản phẩm trong giỏ hàng
                int quantityInCart = rs.getInt("cart_quantity");
                
                // Thêm vào đối tượng Cart
                cart.addItem(new CartItem(p, quantityInCart));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cart;
    }

    /**
     * Thêm một sản phẩm vào giỏ hàng của người dùng trong database.
     * Nếu sản phẩm đã có, nó sẽ cộng dồn số lượng.
     * Nếu chưa có, nó sẽ tạo một dòng mới.
     * @param userId ID người dùng
     * @param productId ID sản phẩm
     * @param quantity Số lượng cần thêm
     */
    public static void addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT quantity FROM UserCarts WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO UserCarts (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE UserCarts SET quantity = ? WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = Accounts.getConnection()) {
            PreparedStatement psCheck = conn.prepareStatement(checkSql);
            psCheck.setInt(1, userId);
            psCheck.setInt(2, productId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) { // Sản phẩm đã có trong giỏ -> Cập nhật
                int currentQuantity = rs.getInt("quantity");
                try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                    psUpdate.setInt(1, currentQuantity + quantity);
                    psUpdate.setInt(2, userId);
                    psUpdate.setInt(3, productId);
                    psUpdate.executeUpdate();
                }
            } else { // Sản phẩm chưa có trong giỏ -> Thêm mới
                try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                    psInsert.setInt(1, userId);
                    psInsert.setInt(2, productId);
                    psInsert.setInt(3, quantity);
                    psInsert.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Cập nhật số lượng của một sản phẩm trong giỏ hàng của user.
     * Được dùng cho chức năng AJAX.
     * @param userId ID người dùng
     * @param productId ID sản phẩm
     * @param newQuantity Số lượng mới
     */
    public static void updateQuantity(int userId, int productId, int newQuantity) {
        if (newQuantity <= 0) {
            // Nếu số lượng mới là 0 hoặc âm, xóa luôn sản phẩm khỏi giỏ
            removeItem(userId, productId);
        } else {
            String sql = "UPDATE UserCarts SET quantity = ? WHERE user_id = ? AND product_id = ?";
            try (Connection conn = Accounts.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, newQuantity);
                ps.setInt(2, userId);
                ps.setInt(3, productId);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Xóa một sản phẩm khỏi giỏ hàng của user trong DB.
     * @param userId ID người dùng
     * @param productId ID sản phẩm
     */
    public static void removeItem(int userId, int productId) {
        String sql = "DELETE FROM UserCarts WHERE user_id = ? AND product_id = ?";
        try (Connection conn = Accounts.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa sạch giỏ hàng của một user (thường dùng sau khi đặt hàng thành công).
     * @param userId ID người dùng
     */
    public static void clearCart(int userId) {
        String sql = "DELETE FROM UserCarts WHERE user_id = ?";
        try (Connection conn = Accounts.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}