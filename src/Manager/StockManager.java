package Manager;

import dao.StockDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Stock;
import ui.Connection;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockManager {

    private StockDAO stockDAO = new StockDAO();


    @FXML
    private TableView<Stock> stockTable;
    @FXML
    private TableColumn<Stock, String> symbolColumn;
    @FXML private TableColumn<Stock, String> nameColumn;
    @FXML private TableColumn<Stock, Double> priceColumn;


    private ScheduledExecutorService service;

    @FXML
    public void initialize() {
        setupTable();
        loadStockDataToTable();
        startPriceSimulation();
        startUIRefresh();
    }

    private void setupTable() {
        stockTable.setEditable(false);
        symbolColumn.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
    }

    private void loadStockDataToTable() {
        List<Stock> stocks = stockDAO.getAllStocks();
        stockTable.setItems(FXCollections.observableArrayList(stocks));
    }

    Random random=new Random();

    void startPriceSimulation(){
        // Simulate stock price changes over time
        // This could involve generating random price changes or using a more complex model
        service= Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            List<Stock> stocks = stockDAO.getAllStocks();
            updateMarketTrend();
            for (Stock stock : stocks) {


                double direction = random.nextBoolean() ? 1 : -1;
                double baseChange = stock.getVolatility() * (random.nextDouble());

                // Apply market trend
                switch (currentTrend) {
                    case BULL -> baseChange += 0.01; // small push up
                    case BEAR -> baseChange -= 0.01; // small push down
                    default -> {}
                }

                double changeFactor = 1 + direction * baseChange;
                double newPrice = stock.getCurrentPrice() * changeFactor;

                newPrice = Math.round(newPrice * 100.0) / 100.0;

                stockDAO.updateStockPrice(stock.getSymbol(),newPrice); // Update the stock price in the database
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    private void startUIRefresh() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            Platform.runLater(this::loadStockDataToTable);
        }, 1, 3, TimeUnit.SECONDS);
    }

    @FXML
    private void backToDashboard(ActionEvent event) throws IOException {
        Connection.showDashboard(event);
    }

    public enum MarketTrend {
        BULL, // Market is rising
        BEAR, // Market is falling
        STABLE  // Market is stable
    }

    private MarketTrend currentTrend = MarketTrend.STABLE;

    private void updateMarketTrend() {
        int roll = random.nextInt(100);
        if (roll < 40) currentTrend = MarketTrend.BULL;
        else if (roll < 80) currentTrend = MarketTrend.BEAR;
        else currentTrend = MarketTrend.STABLE;
    }

    @FXML
    private void goToBuyStock(ActionEvent event) throws IOException {
        Connection.showBuyStock(event);
    }
    @FXML
    private void goToSellStock(ActionEvent event) throws IOException {
        Connection.showSellStock(event);
    }
}
