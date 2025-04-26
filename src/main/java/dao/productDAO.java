package dao;

import controller.Accounts;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.product;

public class productDAO {

    public static List<product> getAll(String keyword) {
        List<product> list = new ArrayList<>();
        try (Connection conn = Accounts.getConnection()) {
            String sql = "SELECT * FROM products";
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql += " WHERE name LIKE ?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                product p = new product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBytes("image"),
                    rs.getString("label"), // lấy label từ DB
                    rs.getInt("quantity")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void add(product p) {
        try (Connection conn = Accounts.getConnection()) {
            String sql = "INSERT INTO products (name, description, price, image, label,quantity) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setBytes(4, p.getImage());
            ps.setString(5, p.getLabel()); // thêm label
            ps.setInt(6, p.getQuantity());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(product p) {
        try (Connection conn = Accounts.getConnection()) {
            String sql = "UPDATE products SET name=?, description=?, price=?, image=?, label=?,quantity=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setBytes(4, p.getImage());
            ps.setString(5, p.getLabel()); // cập nhật label
            ps.setInt(7, p.getId());
            ps.setInt(6, p.getQuantity());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        try (Connection conn = Accounts.getConnection()) {
            String sql = "DELETE FROM products WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static product getProductById(int id) {
        product p = null;
        try (Connection conn = Accounts.getConnection()) {
            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBytes("image"),
                    rs.getString("label"),
                    rs.getInt("quantity")
                    
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    // Trả về danh sách theo nhãn (label)
    public static List<product> getByLabel(String label) {
        List<product> list = new ArrayList<>();
        try (Connection conn = Accounts.getConnection()) {
            String sql = "SELECT * FROM products WHERE label = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, label);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                product p = new product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBytes("image"),
                    rs.getString("label"),
                    rs.getInt("quantity")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
   public static List<product> getFeaturedProducts() {
    List<product> list = new ArrayList<>();
    try (Connection conn = Accounts.getConnection()) {
        String sql = "SELECT * FROM products WHERE label = 'featured'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            product p = new product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getBytes("image"),
                rs.getString("label"),
                rs.getInt("quantity")
            );
            list.add(p);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
public static Map<String, Integer> getProductCountsByLabel() {
    Map<String, Integer> map = new HashMap<>();
    try (Connection conn = Accounts.getConnection()) {
        String sql = "SELECT label, COUNT(*) AS count FROM products GROUP BY label";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String rawLabel = rs.getString("label");
            if (rawLabel == null || rawLabel.trim().isEmpty()) {
                rawLabel = "khac"; // Gán label mặc định nếu null hoặc rỗng
            }
            map.put(rawLabel, rs.getInt("count"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return map;
}
public static List<product> searchProductsByName(String keyword) {
    List<product> list = new ArrayList<>();
    try {
        Connection conn = Accounts.getConnection();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            product p = new product();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getInt("price"));
            p.setDescription(rs.getString("description"));
            p.setLabel(rs.getString("label"));
            list.add(p);
        }
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
public static List<product> searchByKeyword(String keyword) {
    List<product> list = new ArrayList<>();
    try {
        Connection conn = Accounts.getConnection();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            product p = new product();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getInt("price"));
            p.setLabel(rs.getString("label"));
            p.setDescription(rs.getString("description"));
            list.add(p);
        }
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}





}
