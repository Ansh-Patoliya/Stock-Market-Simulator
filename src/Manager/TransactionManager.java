package Manager;


import model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager {

    private final List<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        System.out.println("Transaction added: " + transaction);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions); // Return a copy
    }

    public List<Transaction> getTransactionsByUser(String userEmail) {
        return transactions.stream()
                .filter(t -> t.getUserEmail().equalsIgnoreCase(userEmail))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByStock(String stockSymbol) {
        return transactions.stream()
                .filter(t -> t.getStockSymbol().equalsIgnoreCase(stockSymbol))
                .collect(Collectors.toList());
    }

    public List<Transaction> getBuyTransactions(String userEmail) {
        return transactions.stream()
                .filter(t -> t.getUserEmail().equalsIgnoreCase(userEmail) && t.isBuy())
                .collect(Collectors.toList());
    }

    public List<Transaction> getSellTransactions(String userEmail) {
        return transactions.stream()
                .filter(t -> t.getUserEmail().equalsIgnoreCase(userEmail) && !t.isBuy())
                .collect(Collectors.toList());
    }

    public void printAllTransactions() {
        System.out.println("===== All Transactions =====");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
}
