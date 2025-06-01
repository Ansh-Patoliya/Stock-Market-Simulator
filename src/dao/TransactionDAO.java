package dao;

import dbUtil.DBUtil;
import model.PortfolioItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void logTransaction(int userId, String symbol, String company, int quantity, double price, String type) {
        String sql = "INSERT INTO transactions (user_id, stock_symbol, company_name, quantity, average_price, transaction_type,transaction_time) " +
                "VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, symbol);
            stmt.setString(3, company);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, price);
            stmt.setString(6, type); // "BUY" or "SELL"
            stmt.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis())); // Current timestamp

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PortfolioItem> getTransactionHistory(int userId) {
        Connection conn = null;
        List<PortfolioItem> portfolioItems = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to connect to the database.");
            }
            String sql = "SELECT stock_symbol, company_name, quantity, average_price FROM transactions WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String symbol = rs.getString("stock_symbol");
                String name = rs.getString("company_name");
                int quantity = rs.getInt("quantity");
                double avgPrice = rs.getDouble("average_price");
                portfolioItems.add(new PortfolioItem(symbol, name, quantity, avgPrice));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return portfolioItems;
    }
}
