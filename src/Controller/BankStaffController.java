package Controller;

import Model.BankStaff;
import Model.DBConnection;
import Model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankStaffController {

    // Method to generate unique bank user number (e.g., BS123456)
    private String generateBankUserNumber() {
        Random rand = new Random();
        String bankUserNumber;

        try (Connection conn = DBConnection.getConnection()) {
            boolean exists;
            do {
                bankUserNumber = "BS" + (100000 + rand.nextInt(900000));

                try (PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT 1 FROM bank_staff WHERE bank_user_number = ?")) {
                    checkStmt.setString(1, bankUserNumber);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        exists = rs.next(); // if result exists, try again
                    }
                }

            } while (exists);

            return bankUserNumber;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // Save staff to DB
    public String registerStaff(String name, String email, String password, String branch) {
        String bankUserNumber = generateBankUserNumber();
        if (bankUserNumber == null) return null;

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO bank_staff (bank_user_number, name, email, password, branch) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, bankUserNumber);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, branch);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0 ? bankUserNumber : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public BankStaff login(String bankUserNumber, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bank_staff WHERE bank_user_number = ? AND password = ?");
            stmt.setString(1, bankUserNumber);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new BankStaff(
                        rs.getString("bank_user_number"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("branch")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transaction> getBranchTransactions(String branch) {
        List<Transaction> transactions = new ArrayList<>();
        String query = """
        SELECT t.account_number, t.type, t.amount, t.timestamp, t.related_account
        FROM transactions t
        JOIN customers c ON t.account_number = c.account_number
        WHERE c.branch = ?
        ORDER BY t.timestamp DESC
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, branch);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String accountNumber = rs.getString("account_number");
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String timestamp = rs.getString("timestamp");
                String relatedAccount = rs.getString("related_account");

                transactions.add(new Transaction(accountNumber, type, amount, timestamp, relatedAccount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    // Fetch ATM balance for a branch
    public double getAtmBalance(String branch) {
        String query = "SELECT balance FROM atm_balance WHERE branch = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, branch);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error code
    }

    // Refill ATM of a branch to ₹3,00,000
    public boolean refillAtm(String branch) {
        String query = "UPDATE atm_balance SET balance = 300000 WHERE branch = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, branch);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
