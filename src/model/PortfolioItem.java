package model;

public class PortfolioItem {
    private String stockSymbol;
    private String stockName;
    private int quantity;
    private double averageBuyPrice;
    private double totalValue;


    public PortfolioItem(String stockSymbol, String stockName, int quantity, double averageBuyPrice) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
        this.totalValue = Math.round((quantity * averageBuyPrice)*100.0)/100.0; // Calculate total value based on quantity and average buy price
    }

    // Getters and Setters
    public String getStockSymbol() {
        return stockSymbol;
    }

    public double getTotalValue() {
        return totalValue;
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

    // Helper method to calculate profit/loss
    public double getProfitLoss(double currentPrice) {
        return (currentPrice - averageBuyPrice) * quantity;
    }
}
