package Manager;


import dao.StockDAO;
import dao.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PortfolioItem;
import model.Transaction;
import model.User;
import ui.Connection;
import util.Session;

import java.io.IOException;

public class TransactionManager {
    private TransactionDAO transactionDAO = new TransactionDAO();
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<PortfolioItem, String> stockSymbolColumn;
    @FXML
    private TableColumn<PortfolioItem, Integer> quantityColumn;
    @FXML
    private TableColumn<PortfolioItem, Double> priceColumn;
    @FXML
    private TableColumn<PortfolioItem, Double> totalValueColumn;
    @FXML
    private TableColumn<PortfolioItem, String> transactionTypeColumn;
    @FXML
    private TableColumn<PortfolioItem, String> dateColumn;

    @FXML
    private Label walletLabel;


    private User currentUser;
    private ObservableList<Transaction> transactionsItems;
    private StockDAO stockDAO= new StockDAO();

    public void initialize() {
        transactionsItems = FXCollections.observableArrayList(); // ✅ initialize here

        // then bind to table
        transactionTable.setItems(transactionsItems);
        stockSymbolColumn.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerShare"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        setCurrentUser(); // Set current user and load portfolio data
        refreshTransactionHistory();
    }

    private void refreshTransactionHistory() {
        transactionsItems.clear(); // Clear existing items
        if (currentUser != null) {
            transactionsItems.addAll(transactionDAO.getTransactionHistory(currentUser.getId()));
            System.out.println("Transaction history refreshed for user: " + currentUser.getEmail());
        } else {
            System.out.println("⚠️ Current user is null, cannot refresh transaction history.");
        }
    }

    public void setCurrentUser() {
        this.currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            updateWalletLabel();
        } else {
            System.out.println("⚠️ Current user is null");
        }
    }

    private void updateWalletLabel() {
        walletLabel.setText("₹" + currentUser.getWalletBalance());
    }


    public void handleBackToDashBoard(ActionEvent event) throws IOException {
        Connection.showDashboard(event);
    }

    public void handleBuyStocks(ActionEvent event) throws IOException {
        Connection.showBuyStock(event);
    }

    public void handleSellStocks(ActionEvent event) throws IOException {
        Connection.showSellStock(event);
    }
}
