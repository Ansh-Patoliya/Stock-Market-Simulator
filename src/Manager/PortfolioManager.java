package Manager;

import dao.PortfolioDAO;
import dao.StockDAO;
import dao.TransactionDAO;
import javafx.application.Platform;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private TableColumn<PortfolioItem, Double> profitLossColumn;
    @FXML
    private TableColumn<PortfolioItem, Double> averageBuyPriceColumn;

    @FXML
    private Label walletLabel;

    TransactionDAO transactionDAO = new TransactionDAO();

    private User currentUser;
    private ObservableList<PortfolioItem> portfolioItems;
    private StockDAO stockDAO= new StockDAO();

    public void initialize() {
        portfolioItems = FXCollections.observableArrayList(); // ✅ initialize here

        // then bind to table
        portfolioTable.setItems(portfolioItems);
        stockSymbolColumn.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        companyNameColumn.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        averageBuyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("averageBuyPrice"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        profitLossColumn.setCellValueFactory(new PropertyValueFactory<>("profitLoss"));

        startAutoRefresh(); // Start auto-refreshing portfolio prices
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
        portfolioItems.addAll(portfolioDAO.getPortfolioItems(currentUser.getId()));
        portfolioTable.setItems(portfolioItems);
    }

    private void refreshPortfolioPrices() {
        for (PortfolioItem item : portfolioItems) {
            double livePrice = stockDAO.getStockBySymbol(item.getStockSymbol()).getCurrentPrice();

            // Set current price dynamically
            item.setCurrentPrice(livePrice); // update current price
            // Also calculate updated total value manually
            item.setTotalValue(Math.round((livePrice * item.getQuantity()) * 100.0) / 100.0); // update total value to 2 decimal places
            item.setProfitLoss(Math.round((livePrice - item.getAverageBuyPrice()) * item.getQuantity() * 100.0) / 100.0); // update profit/loss to 2 decimal places
        }
        portfolioTable.refresh(); // refresh table to show new values
    }

    ScheduledExecutorService scheduler;

    private void startAutoRefresh() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                refreshPortfolioPrices();
            });
        }, 0, 2, TimeUnit.SECONDS); // Every 2 seconds
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
