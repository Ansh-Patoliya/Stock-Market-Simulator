package Manager;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import ui.Connection;
import util.Session;
import util.Validator;

import java.io.IOException;
import java.time.LocalDate;

public class UserManager {
    private UserDAO userDAO = new UserDAO();
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private TextField forgotEmailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField amountField;

    public void registration(ActionEvent actionEvent) {
        // Get input values from the fields
        String username = usernameField.getText();
        String email = emailField.getText();
        LocalDate date = dateOfBirth.getValue();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input fields
        String errorMessage = Validator.validateRegistration(username, email, date, password, confirmPassword);

        if (errorMessage != null) {
            showAlert(AlertType.ERROR, "Error", errorMessage);
            return;
        }

        userDAO.addUser(new User(username, date, email, password));
        showAlert(AlertType.INFORMATION, "Success", "User registered successfully.");

        try {
            Connection.handleBackToLogin(actionEvent);
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to navigate back to login screen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        Connection.handleRegister(event);
    }


    @FXML
    private void login(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please enter both username and password.");
            return;
        }
        // Validate user credentials
        User user = userDAO.getUserByEmail(email);
        Session.setCurrentUser(user);
        // Check if user exists and password matches
        if (user != null && user.getPassword().equals(password)) {
            // Login successful, proceed to the next screen
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getFullName() + "!");
            // Here you can redirect to the dashboard or main application screen
            // For example, you can create a new instance of DashboardScreen and show it
            try {
                Connection.showDashboard(event);
            } catch (IOException e) {
                showAlert(AlertType.ERROR, "Error", "Failed to navigate to the dashboard.");
                e.printStackTrace();
            }
        } else {    // Show an error message
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }


    private void showAlert(AlertType alertType, String title, String message) {
        Connection.showAlert(alertType, title, message);
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) throws IOException {
        Connection.handleForgotPassword(event);
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) throws IOException {
        Connection.handleBackToLogin(event);
    }

    public void handleViewDashboard(ActionEvent event) throws IOException {
        Connection.showDashboard(event);
    }

    public void forgotPassword(ActionEvent actionEvent) {
        String email = forgotEmailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            showAlert(AlertType.ERROR, "Error", "User not found.");
            return;
        }
        user.setPassword(password);
        if (userDAO.updateUserPassword(user)) {
            showAlert(AlertType.INFORMATION, "Success", "Password updated successfully.");
            try {
                Connection.handleBackToLogin(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to update password.");
        }
    }

    public void handleAddBalance(ActionEvent event) throws IOException {
        Connection.addBalance(event);
    }

    public void handleWithdrawBalance(ActionEvent event) throws IOException {
        Connection.withdrawBalance(event);
    }


    public void addWalletBalance(ActionEvent event) {
        User user=Session.getCurrentUser();
        if(user!=null) {
            double newBalance = Double.parseDouble(amountField.getText());
            if (userDAO.updateWalletBalance(user.getEmail(), user.getWalletBalance() + newBalance)) {
                showAlert(AlertType.INFORMATION, "Success", "Wallet balance updated successfully.");
                user.setWalletBalance(user.getWalletBalance() + newBalance);
                try {
                    Connection.showDashboard(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to update wallet balance.");
            }
        }
        else {
            showAlert(AlertType.ERROR, "Error", "User not logged in.");
        }
    }

    public void withdrawWalletBalance(ActionEvent event) {
        User currentUser=Session.getCurrentUser();
        double withdrawalAmount = Double.parseDouble(amountField.getText());
        if (withdrawalAmount <= 0) {
            showAlert(AlertType.ERROR, "Error", "Withdrawal amount must be greater than zero.");
            return;
        }
        if (currentUser.getWalletBalance() < withdrawalAmount) {
            showAlert(AlertType.ERROR, "Error", "Insufficient wallet balance.");
            return;
        }
        double newBalance = currentUser.getWalletBalance() - withdrawalAmount;
        if (userDAO.updateWalletBalance(currentUser.getEmail(), newBalance)) {
            showAlert(AlertType.INFORMATION, "Success", "Withdrawal successful. New balance: " + newBalance);
            currentUser.setWalletBalance(newBalance);
            try {
                Connection.showDashboard(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to withdraw from wallet.");
        }
    }
}
