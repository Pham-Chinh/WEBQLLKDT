package controller;
import java.sql.Connection;
import java.sql.DriverManager;

public class Accounts {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/dbproject", "root", "123456"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   
}

