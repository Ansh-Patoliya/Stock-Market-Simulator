package Manager;

import dao.StockDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import model.Stock;
import model.User;
import dao.PortfolioDAO;
import ui.Connection;
import util.Session;

import javafx.scene.input.KeyEvent;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

import static ui.Connection.showAlert;

public class BuyStockManger {
    @FXML
    private ComboBox<String> stockComboBox;

    @FXML
    private Label priceLabel, totalCostLabel, walletBalanceLabel;


    @FXML
    private TextField quantityField;

    private StockDAO stockDAO = new StockDAO();
    private PortfolioDAO portfolioDAO = new PortfolioDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    private Stock selectedStock;
    private User currentUser;

    @FXML
    public void initialize() {
        // Get logged-in user
        currentUser = Session.getCurrentUser(); // Helper class to keep current user
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User not logged in.");
            return;
        }

        walletBalanceLabel.setText("₹" + currentUser.getWalletBalance());

        // Load all stock symbols
        List<Stock> stocks = stockDAO.getAllStocks();
        for (Stock stock : stocks) {
            stockComboBox.getItems().add(stock.getSymbol());
        }

        stockComboBox.setOnAction(event -> updateStockDetails());
        quantityField.setOnKeyReleased(this::calculateTotalCost);
    }

    private void updateStockDetails() {
        String symbol = stockComboBox.getValue();
        selectedStock = stockDAO.getStockBySymbol(symbol);
        if (selectedStock != null) {
            priceLabel.setText("₹" + selectedStock.getCurrentPrice());
            calculateTotalCost(null);
        }
    }

    private void calculateTotalCost(KeyEvent event) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            double total = Math.round(quantity * selectedStock.getCurrentPrice()*100.0) / 100.0;
            totalCostLabel.setText("₹" + total);
        } catch (Exception e) {
            totalCostLabel.setText("₹0.00");
        }
    }

    public void handleBuy(ActionEvent event) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity must be greater than zero.");
                return;
            }

            double currentPrice = selectedStock.getCurrentPrice();
            double totalCost = Math.round((quantity * currentPrice)*100.0) / 100.0;
            if (currentUser.getWalletBalance() < totalCost) {
                showAlert(Alert.AlertType.ERROR, "Error", "Insufficient wallet balance.");
                return;
            }

            boolean success = portfolioDAO.buyStock(currentUser, selectedStock, quantity);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Stock purchased successfully!");
                transactionDAO.logTransaction(currentUser.getId(), selectedStock.getSymbol(),selectedStock.getName(),quantity, currentPrice, "BUY");
                walletBalanceLabel.setText("₹" + currentUser.getWalletBalance());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to purchase stock.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity entered.");
        }
    }

    public void handleBackToDashBoard(ActionEvent event) throws IOException {
        Connection.showDashboard(event);
    }
}
