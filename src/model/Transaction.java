package model;

import java.time.LocalDateTime;

public class Transaction {

    private String userEmail;
    private String stockSymbol;
    private String stockName;
    private int quantity;
    private double pricePerShare;
    private boolean isBuy;
    private LocalDateTime timestamp;

    public Transaction(String userEmail, String stockSymbol, String stockName, int quantity, double pricePerShare, boolean isBuy) {
        this.userEmail = userEmail;
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.isBuy = isBuy;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getTotalAmount() {
        return pricePerShare * quantity;
    }

    @Override
    public String toString() {
        return (isBuy ? "BUY" : "SELL") + " | " + quantity + " shares of " + stockSymbol +
                " at $" + pricePerShare + " | Total: $" + getTotalAmount() +
                " | User: " + userEmail + " | Time: " + timestamp;
    }
}
