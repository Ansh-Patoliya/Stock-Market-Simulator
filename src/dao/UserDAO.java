
package dao;

import dbUtil.DBUtil;
import model.User;

import java.sql.*;

/**
 * Data Access Object (DAO) class for handling User entity database operations.
 * This class provides methods to perform CRUD operations on the users table
 * in the database, managing user data persistence and retrieval.
 */
public class UserDAO {

    /**
     * Adds a new user to the database.
     *
     * @param user The User object containing user details to be added
     * @return true if user was successfully added, false otherwise
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, dob, email, wallet_balance) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPassword());
            ps.setDate(3, Date.valueOf(user.getDateOfBirth()));
            ps.setString(4, user.getEmail());
            ps.setDouble(5, user.getWalletBalance());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Retrieves a user from the database by their email address.
     *
     * @param email The email address of the user to retrieve
     * @return User object if found, null if no user exists with the given email
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id")); // Ensure to set the ID if needed
                user.setFullName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setDateOfBirth(rs.getDate("dob").toLocalDate());
                user.setPassword(rs.getString("password"));
                user.setWalletBalance(rs.getDouble("wallet_balance")); // 🔥 This is IMPORTANT!
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the wallet balance of a user identified by their email.
     *
     * @param email      The email address of the user whose balance needs to be updated
     * @param newBalance The new balance amount to set
     * @return true if balance was successfully updated, false otherwise
     */
    public boolean updateWalletBalance(String email, double newBalance) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET wallet_balance = ? WHERE email = ?")) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserPassword(User user) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Add updateUser, deleteUser, getAllUsers, etc.
}
