/**
 * Represents a user in the stock market application.
 * This class manages user information and wallet operations for trading stocks.
 *
 * Each user has:
 * - Personal information (full name, date of birth, email)
 * - Authentication credentials (password)
 * - A wallet balance for trading (initialized with 10,000 by default)
 *
 * The class provides methods for:
 * - Getting and setting user information
 * - Managing wallet balance (adding and deducting funds)
 * - String representation of user data
 */

package model;

import java.time.LocalDate;

public class User {
    private int id; // Unique identifier for the user
    private String fullName;
    private LocalDate dateOfBirth;
    private String email;
    private double walletBalance;
    private String password;

    public User(int id, String fullName, LocalDate dateOfBirth, String email, double walletBalance, String password) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.walletBalance = walletBalance;
        this.password = password;
    }

    public User(String fullName, LocalDate dateOfBirth, String email, String password) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.walletBalance = 10000; // Default balance
        this.password = password;
    }
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public double getWalletBalance() {
        return walletBalance;
    }
    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFunds(double amount) {
        if (amount > 0) {
            this.walletBalance += amount;
        }
    }

    public boolean deductFunds(double amount) {
        if (amount > 0 && amount <= walletBalance) {
            walletBalance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", walletBalance=" + walletBalance +
                ", password='" + password + '\'' +
                '}';
    }
}

