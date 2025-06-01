package Manager;

import dao.PortfolioDAO;
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
import model.User;
import ui.Connection;
import util.Session;

import java.io.IOException;

public class PortfolioManager {
    PortfolioDAO portfolioDAO = new PortfolioDAO();
    @FXML
    private TableView<PortfolioItem> portfolioTable;
    @FXML
    private TableColumn<PortfolioItem, String> stockSymbolColumn;
    @FXML
    private TableColumn<PortfolioItem, String> companyNameColumn;
    @FXML
    private TableColumn<PortfolioItem, Integer> quantityColumn;
    @FXML
    private TableColumn<PortfolioItem, Double> currentPriceColumn;
    @FXML
    private TableColumn<PortfolioItem, Double> totalValueColumn;

    @FXML
    private Label walletLabel;

    TransactionDAO transactionDAO = new TransactionDAO();

    private User currentUser;
    private ObservableList<PortfolioItem> portfolioItems;

    public void initialize() {
        portfolioItems = FXCollections.observableArrayList(); // ✅ initialize here

        // then bind to table
        portfolioTable.setItems(portfolioItems);
        stockSymbolColumn.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        companyNameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("averageBuyPrice"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));


    }


    public void setCurrentUser() {
        this.currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            loadPortfolioData();
            updateWalletLabel();
        } else {
            System.out.println("⚠️ Current user is null");
        }
    }

    private void updateWalletLabel() {
        walletLabel.setText("₹" + currentUser.getWalletBalance());
    }

    private void loadPortfolioData() {
        portfolioItems.addAll(transactionDAO.getTransactionHistory(currentUser.getId()));
        portfolioTable.setItems(portfolioItems);


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
