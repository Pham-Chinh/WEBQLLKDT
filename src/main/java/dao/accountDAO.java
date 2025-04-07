package dao;

import controller.Accounts;
import java.sql.*;
import java.util.List;
import model.taikhoan;
import java.util.ArrayList;

public class accountDAO {

    // Lấy toàn bộ tài khoản
    public static List<taikhoan> getAllAccount() {
        return getAllAccount(null); // Gọi lại hàm dưới nếu không có search
    }

    // Lấy tài khoản có tìm kiếm theo username
    public static List<taikhoan> getAllAccount(String search) {
        List<taikhoan> list = new ArrayList<>();
        try (Connection conn = Accounts.getConnection()) {
            String sql = "SELECT * FROM accounts";
            if (search != null && !search.trim().isEmpty()) {
                sql += " WHERE username LIKE ?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(1, "%" + search + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taikhoan tk = new taikhoan();
                tk.setId(rs.getInt("id"));
                tk.setUsername(rs.getString("username"));
               
                tk.setPassword(rs.getString("password"));
                tk.setRole(rs.getString("role"));
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Xóa tài khoản theo ID
    public static void deleteAccount(int id) {
        try (Connection conn = Accounts.getConnection()) {
            String sql = "DELETE FROM accounts WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateAccount(taikhoan acc) {
    try (Connection conn = Accounts.getConnection()) {
        String sql = "UPDATE accounts SET username=?, password=?, role=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, acc.getUsername());
        ps.setString(2, acc.getPassword());
        ps.setString(3, acc.getRole());
        ps.setInt(4, acc.getId());
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public static void addAccount(taikhoan acc) {
    try (Connection conn = Accounts.getConnection()) {
        String sql = "INSERT INTO accounts (username, password, role) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, acc.getUsername());
        ps.setString(2, acc.getPassword());
        ps.setString(3, acc.getRole());
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


}
