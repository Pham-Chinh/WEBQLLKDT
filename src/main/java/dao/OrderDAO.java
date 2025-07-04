package dao;

import controller.Accounts; // Sử dụng lớp kết nối DB của bạn
import model.Cart;
import model.CartItem;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.product;

public class OrderDAO {

    // --- CÁC PHƯƠNG THỨC ĐÃ ĐƯỢC VIẾT LẠI THEO PHONG CÁCH CỦA BẠN ---

    /**
     * Lấy tất cả các đơn hàng, sắp xếp theo ngày mới nhất.
     * @return Danh sách đơn hàng.
     */
    public static List<Order> getOrdersByUserId(int userId) {
    List<Order> list = new ArrayList<>();
    String sql = "SELECT * FROM Orders WHERE user_id = ? ORDER BY order_date DESC";
    
    try (Connection conn = Accounts.getConnection(); // Sử dụng lớp kết nối Accounts của bạn
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            // Dùng constructor đầy đủ của lớp Order
            Order order = new Order(
               rs.getInt("order_id"),
                (Integer) rs.getObject("user_id"),
                rs.getString("customer_name"),
                rs.getString("customer_phone"),
                rs.getString("shipping_address"),
                rs.getString("notes"),
                rs.getTimestamp("order_date"),
                rs.getDouble("total_amount"),
                rs.getString("payment_method"),
                rs.getString("status")
            );
            list.add(order);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    /**
     * Phương thức quan trọng để lưu đơn hàng mới.
     * Vì nó thao tác trên 2 bảng (Orders và OrderDetails), chúng ta BẮT BUỘC phải dùng Transaction.
     * Đây là điểm khác biệt duy nhất so với các hàm add/update đơn giản trong accountDAO.
     */
    public static int saveOrder(Cart cart, String name, String phone, String address, String notes, String paymentMethod,Integer userId) {
        String sqlOrder = "INSERT INTO Orders (customer_name, customer_phone, shipping_address, notes, total_amount, payment_method,user_id) VALUES (?, ?, ?, ?, ?, ?,?)";
        String sqlOrderDetail = "INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = Accounts.getConnection();
            // --- BẮT ĐẦU TRANSACTION ---
            // Tắt chế độ tự động commit để kiểm soát toàn bộ quá trình
            conn.setAutoCommit(false); 

            // 1. LƯU VÀO BẢNG Orders
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setString(1, name);
            psOrder.setString(2, phone);
            psOrder.setString(3, address);
            psOrder.setString(4, notes);
            psOrder.setDouble(5, cart.getTotalAmount());
            psOrder.setString(6, paymentMethod);
              if (userId != null && userId > 0) {
            psOrder.setInt(7, userId);
        } else {
            psOrder.setNull(7, java.sql.Types.INTEGER);
        }
        psOrder.executeUpdate();
        

            // Lấy order_id vừa được tạo
            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            } else {
                throw new SQLException("Tạo đơn hàng thất bại, không có ID được trả về.");
            }

            // 2. LƯU VÀO BẢNG OrderDetails
            PreparedStatement psDetail = conn.prepareStatement(sqlOrderDetail);
            for (CartItem item : cart.getItems()) {
                psDetail.setInt(1, orderId);
                psDetail.setInt(2, item.getProduct().getId());
                psDetail.setInt(3, item.getQuantity());
                psDetail.setDouble(4, item.getProduct().getPrice());
                psDetail.addBatch();
            }
            psDetail.executeBatch();

            // Nếu tất cả các lệnh trên thành công, xác nhận và lưu lại tất cả thay đổi
            conn.commit(); 
            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Nếu có bất kỳ lỗi nào, hủy bỏ tất cả các thay đổi đã thực hiện
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0; // Trả về 0 nếu thất bại
        } finally {
             try {
                // Luôn bật lại auto-commit và đóng kết nối
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // Thêm phương thức này vào file dao/orderDAO.java

/**
 * Tìm kiếm các đơn hàng dựa trên tên khách hàng.
 * @param customerName Tên khách hàng cần tìm (có thể là một phần của tên).
 * @return Danh sách các đơn hàng phù hợp.
 */
public List<Order> searchOrdersByCustomerName(String customerName) {
    List<Order> list = new ArrayList<>();
    // Sắp xếp theo ngày mới nhất lên đầu
    String sql = "SELECT * FROM Orders WHERE customer_name LIKE ? ORDER BY order_date DESC";

    try (Connection conn = Accounts.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // Thêm dấu % để tìm kiếm gần đúng (chứa chuỗi)
        ps.setString(1, "%" + customerName + "%");

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = new Order(
               rs.getInt("order_id"),
                (Integer) rs.getObject("user_id"),
                rs.getString("customer_name"),
                rs.getString("customer_phone"),
                rs.getString("shipping_address"),
                rs.getString("notes"),
                rs.getTimestamp("order_date"),
                rs.getDouble("total_amount"),
                rs.getString("payment_method"),
                rs.getString("status")
                );
                list.add(order);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    // Các phương thức khác có thể cần trong tương lai, viết theo đúng style của bạn
    
    public static void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = Accounts.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Thêm vào file dao/orderDAO.java
public static Order getOrderById(int orderId) {
    String sql = "SELECT * FROM Orders WHERE order_id = ?";
    try (Connection conn = Accounts.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Order(
               rs.getInt("order_id"),
                (Integer) rs.getObject("user_id"),
                rs.getString("customer_name"),
                rs.getString("customer_phone"),
                rs.getString("shipping_address"),
                rs.getString("notes"),
                rs.getTimestamp("order_date"),
                rs.getDouble("total_amount"),
                rs.getString("payment_method"),
                rs.getString("status")
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
public static List<CartItem> getOrderDetailsById(int orderId) {
    List<CartItem> list = new ArrayList<>();
    String sql = "SELECT p.*, od.quantity as order_quantity, od.price as price_at_purchase " +
                 "FROM OrderDetails od JOIN products p ON od.product_id = p.id " +
                 "WHERE od.order_id = ?";
    
    try (Connection conn = Accounts.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            product p = new product();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            // Lấy giá tại thời điểm mua hàng thay vì giá hiện tại của sản phẩm
            p.setPrice(rs.getDouble("price_at_purchase")); 
            
            int quantityInOrder = rs.getInt("order_quantity");
            list.add(new CartItem(p, quantityInOrder));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    
    // Tạm thời chưa cần hàm delete order vì nó khá nguy hiểm và cần xử lý phức tạp (xóa cả details)
}