package dao;

import model.PortfolioItem;
import model.Stock;
import model.User;
import dbUtil.DBUtil;
import util.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortfolioDAO {

    public boolean buyStock(User user, Stock stock, int quantity) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double totalCost = stock.getCurrentPrice() * quantity;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check wallet balance
            double currentBalance = user.getWalletBalance();
            if (currentBalance < totalCost) {
                return false; // Not enough balance
            }

            // 2. Deduct wallet balance
            double newBalance = Math.round((currentBalance - totalCost)*100.0) / 100.0;
            ps = conn.prepareStatement("UPDATE users SET wallet_balance = ? WHERE email = ?");
            ps.setDouble(1, newBalance);
            ps.setString(2, user.getEmail());
            ps.executeUpdate();

            // 3. Check if user already owns the stock
            ps = conn.prepareStatement("SELECT * FROM portfolio WHERE user_id = ? AND stock_symbol = ?");
            ps.setInt(1, user.getId());
            ps.setString(2, stock.getSymbol());
            rs = ps.executeQuery();

            if (rs.next()) {
                // Already owns, update quantity and average price
                int existingQty = rs.getInt("quantity");
                double existingAvg = rs.getDouble("average_price");
                int totalQty = existingQty + quantity;
                double newAvgPrice = Math.round(((existingAvg * existingQty) + (stock.getCurrentPrice() * quantity)) / totalQty)* 100.0 / 100.0;

                ps = conn.prepareStatement("UPDATE portfolio SET quantity = ?, average_price = ? WHERE user_id = ? AND stock_symbol = ?");
                ps.setInt(1, totalQty);
                ps.setDouble(2, newAvgPrice);
                ps.setInt(3, user.getId());
                ps.setString(4, stock.getSymbol());
                ps.executeUpdate();
            } else {
                // New stock entry
                ps = conn.prepareStatement("INSERT INTO portfolio (user_id, stock_symbol, quantity, average_price) VALUES (?, ?, ?, ?)");
                ps.setInt(1, user.getId());
                ps.setString(2, stock.getSymbol());
                ps.setInt(3, quantity);
                ps.setDouble(4, stock.getCurrentPrice());
                ps.executeUpdate();
            }

            conn.commit();
            user.setWalletBalance(newBalance); // Update user object in memory
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public boolean sellStock(User user, Stock stock, int quantity) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check if user owns the stock
            ps = conn.prepareStatement("SELECT * FROM portfolio WHERE user_id = ? AND stock_symbol = ?");
            ps.setInt(1, user.getId());
            ps.setString(2, stock.getSymbol());
            rs = ps.executeQuery();

            if (!rs.next()) {
                return false; // User does not own this stock
            }

            int existingQty = rs.getInt("quantity");
            if (existingQty < quantity) {
                return false; // Not enough stock to sell
            }

            // 2. Calculate total sale amount
            double saleAmount = stock.getCurrentPrice() * quantity;

            // 3. Update wallet balance
            double newBalance = Math.round((user.getWalletBalance() + saleAmount)*100.0 )/ 100.0;
            ps = conn.prepareStatement("UPDATE users SET wallet_balance = ? WHERE email = ?");
            ps.setDouble(1, newBalance);
            ps.setString(2, user.getEmail());
            ps.executeUpdate();

            // 4. Update portfolio
            if (existingQty == quantity) {
                // Remove stock from portfolio
                ps = conn.prepareStatement("DELETE FROM portfolio WHERE user_id = ? AND stock_symbol = ?");
                ps.setInt(1, user.getId());
                ps.setString(2, stock.getSymbol());
                ps.executeUpdate();
            } else {
                // Update quantity
                int newQty = existingQty - quantity;
                ps = conn.prepareStatement("UPDATE portfolio SET quantity = ? WHERE user_id = ? AND stock_symbol = ?");
                ps.setInt(1, newQty);
                ps.setInt(2, user.getId());
                ps.setString(3, stock.getSymbol());
                ps.executeUpdate();
            }

            conn.commit();
            user.setWalletBalance(newBalance); // Update user object in memory
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    public List<String> getStockSymbol(int user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> stockSymbols = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT stock_symbol FROM portfolio WHERE user_id = ?");
            ps.setInt(1, user);
            rs = ps.executeQuery();

            while (rs.next()) {
                stockSymbols.add(rs.getString("stock_symbol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }

        return stockSymbols;
    }

    public List<PortfolioItem> getPortfolioItems(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PortfolioItem> portfolioItems = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT p.stock_symbol, s.name AS stock_name, p.quantity, p.average_price " +
                    "FROM portfolio p JOIN stocks s ON p.stock_symbol = s.symbol WHERE p.user_id = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                String stockSymbol = rs.getString("stock_symbol");
                String stockName = rs.getString("stock_name");
                int quantity = rs.getInt("quantity");
                double averageBuyPrice = rs.getDouble("average_price");

                PortfolioItem item = new PortfolioItem(stockSymbol, stockName, quantity, averageBuyPrice);
                portfolioItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }

        return portfolioItems;
    }
}
