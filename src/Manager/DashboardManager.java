package Manager;

import javafx.fxml.FXML;
import ui.Connection;

import javafx.event.ActionEvent;
import util.Session;

import java.io.IOException;

public class DashboardManager {



    @FXML
    private static void showDashboard(ActionEvent event) throws IOException {
        Connection.showDashboard(event);
    }


    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        Session.clearSession();
        Connection.handleBackToLogin(event);
    }

    public void handleViewStocks(ActionEvent event) throws IOException {
        Connection.viewStocks(event);
    }

    public void handleBalance(ActionEvent event) throws IOException {
        Connection.balance(event);
    }

    public void handlePortfolio(ActionEvent event) throws IOException {
        Connection.showPortfolio(event);
    }

    public void handleTransactions(ActionEvent event) throws IOException {
        Connection.showTransactionHistory(event);
    }
}
