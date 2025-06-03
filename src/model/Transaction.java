package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Transaction {

    private String userEmail;
    private String stockSymbol;
    private String stockName;
    private int quantity;
    private double pricePerShare;
    private String transactionType; // "BUY" or "SELL"
    private Timestamp timestamp;
    private double totalValue; // Total value of the portfolio item

    public Transaction( String stockSymbol, int quantity, double pricePerShare, String transactionType,Timestamp timestamp) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        totalValue=this.getTotalAmount();
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public void setPricePerShare(double pricePerShare) {
        this.pricePerShare = pricePerShare;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotalAmount() {
        return pricePerShare * quantity;
    }

}
