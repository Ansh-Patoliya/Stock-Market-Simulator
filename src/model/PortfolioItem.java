package model;

public class PortfolioItem {
    private String stockSymbol;
    private String stockName;
    private int quantity;
    private double averageBuyPrice;
    private double currentPrice; // Current price of the stock, can be updated dynamically
    private double totalValue; // Total value of the portfolio item
    private double profitLoss;

    public PortfolioItem(String stockSymbol, String stockName, int quantity, double averageBuyPrice) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
    }

    // Getters and Setters
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

    public double getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void setAverageBuyPrice(double averageBuyPrice) {
        this.averageBuyPrice = averageBuyPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public double getProfitLoss() {
        return profitLoss;
    }
}
