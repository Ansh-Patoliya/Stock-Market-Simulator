package dao;

import model.Stock;
import dbUtil.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stocks";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stocks.add(new Stock(
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getDouble("current_price")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public void updateStockPrice(String symbol, double newPrice) {
        String sql = "UPDATE stocks SET current_price = ? WHERE symbol = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, symbol);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Stock getStockBySymbol(String symbol) {
        Stock stock = null;
        String sql = "SELECT * FROM stocks WHERE symbol = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, symbol);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stock = new Stock(
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getDouble("current_price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }
}
