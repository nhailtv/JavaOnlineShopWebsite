package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Java.User;

public class UserDAO {
    private Connection conn;
    private String query;
    private PreparedStatement stmt;
    private ResultSet rs;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public User userLogin(String email, String password) {
        User user = null;
        try {
            query = "SELECT * FROM user WHERE email = ? AND password = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public String registerUser(String email, String password, String phoneNumber, String address) {
        try {
            String query = "SELECT COUNT(*) FROM user WHERE email = ? OR phoneNumber = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, phoneNumber);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();

            if (count > 0) {
                // Email or phone number already exists
                return "Email or Phone number already exists";
            } else {
                query = "INSERT INTO user (email, password, phoneNumber, address) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, address);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    return null; // Registration successful
                } else {
                    return "Something went wrong, please try again";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong, please try again";
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            query = "SELECT * FROM user";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setAddress(rs.getString("address"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public boolean deleteUser(String email) {
        try {
            query = "DELETE FROM user WHERE email = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public User getUserByEmail(String email) {
        User user = null;
        try {
            query = "SELECT * FROM user WHERE email = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setAddress(rs.getString("address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
