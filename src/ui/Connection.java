package ui;

import Manager.PortfolioManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Connection {
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: remove header
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public static void handleRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/RegistrationPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Registration");
    }

    @FXML
    public static void handleForgotPassword(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/ForgotPassword.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Forgot Password");
    }

    @FXML
    public static void handleBackToLogin(ActionEvent event) throws IOException {


        showAlert(Alert.AlertType.INFORMATION, "back", "Returning to login screen.");
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");

    }

    @FXML
    public static void showDashboard(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/Dashboard.fxml"));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//        stage.setTitle("Dashboard");

        FXMLLoader loader = new FXMLLoader(Connection.class.getResource("/XML/Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();

    }

    @FXML
    public static void viewStocks(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/ViewStocks.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Stocks");
        stage.show();
    }

    @FXML
    public static void balance(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/Balance.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Balance");
        stage.show();
    }

    @FXML
    public static void addBalance(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/AddBalance.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Add Balance");
        stage.show();
    }

    @FXML
    public static void withdrawBalance(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/WithdrawBalance.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Withdraw Balance");
        stage.show();
    }

    @FXML
    public static void showBuyStock(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/Buy.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Buy Stock");
        stage.show();
    }

    public static void showSellStock(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/Sell.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Sell Stock");
        stage.show();
    }

    @FXML
    public static void showPortfolio(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Connection.class.getResource("/XML/Portfolio.fxml"));
        Parent root = loader.load(); // load FXML

        PortfolioManager controller = loader.getController(); // get controller
        controller.setCurrentUser(); // safe call AFTER Session is ready

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Portfolio");
        stage.show();
    }
    @FXML
    public static void showTransactionHistory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Connection.class.getResource("/XML/Transaction.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Transaction History");
        stage.show();
    }
}
