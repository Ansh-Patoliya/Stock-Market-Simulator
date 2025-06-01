package Manager;

import dao.PortfolioDAO;
import dao.StockDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.Stock;
import model.User;
import ui.Connection;
import util.Session;

import java.io.IOException;
import java.util.List;

import static ui.Connection.showAlert;

public class

SellStockManager {

    @FXML
    private ComboBox<String> stockComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private Label totalLabel, walletBalanceLabel,priceLabel;

    private final StockDAO stockDAO = new StockDAO();
    private final PortfolioDAO portfolioDAO = new PortfolioDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private Stock selectedStock;
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = Session.getCurrentUser(); // Helper class to keep current user
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User not logged in.");
            return;
        }

        walletBalanceLabel.setText("₹" + currentUser.getWalletBalance());
        List<String> userStocks = portfolioDAO.getStockSymbol(currentUser.getId());
        stockComboBox.getItems().addAll(userStocks);

        stockComboBox.setOnAction(e -> updateStockDetails());
        quantityField.setOnKeyReleased(this::updateTotal);

        // Load stocks that user owns
    }

    private void updateTotal(KeyEvent event) {
        try {
            String symbol = stockComboBox.getValue();
            if (symbol == null) return;
            int quantity = Integer.parseInt(quantityField.getText());
            double price = stockDAO.getStockBySymbol(symbol).getCurrentPrice();
            totalLabel.setText("₹" + Math.round((quantity * price)*100.0 )/ 100.0);
        } catch (Exception e) {
            totalLabel.setText("₹0.0");
        }
    }

    private void updateStockDetails() {
        String symbol = stockComboBox.getValue();
        selectedStock = stockDAO.getStockBySymbol(symbol);
        if (selectedStock != null) {
            priceLabel.setText("₹" + selectedStock.getCurrentPrice());
            updateTotal(null);
        }
    }


    public void handleSell(ActionEvent event) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity must be greater than zero.");
                return;
            }

            double currentPrice = selectedStock.getCurrentPrice();
            boolean success=portfolioDAO.sellStock(currentUser, selectedStock, quantity);
            if (!success) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to sell stock. Check your portfolio.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Sold " + quantity + " shares of " + selectedStock.getSymbol() + ".");
            transactionDAO.logTransaction(currentUser.getId(), selectedStock.getSymbol(),selectedStock.getName(),quantity, currentPrice, "SELL");
            walletBalanceLabel.setText("₹" + currentUser.getWalletBalance());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity entered.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void handleBackToDashBoard(ActionEvent event) throws IOException {
            Connection.showDashboard(event);
    }
}
