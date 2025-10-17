package Controller;

import Model.Customer;
import Model.Loan;
import Model.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LoanController {

    // Check if customer is eligible and already has no active loan
    public static boolean isEligibleForLoan(Customer customer) {
        if (!customer.getEmploymentStatus().equalsIgnoreCase("employed")) {
            return false;
        }
        return customer.getMonthlySalary() >= 200000;
    }

    public static boolean hasActiveLoan(String accountNumber) {
        String query = "SELECT COUNT(*) FROM loans WHERE account_number = ? AND status = 'Active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Loan> getAllLoansForBranch(String branch) {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT l.loan_id, l.account_number, l.loan_type, l.amount, l.duration_years, l.interest_rate, l.emi, " +
                "l.start_date, l.status, l.last_payment_date, l.total_paid, l.fine " +
                "FROM loans l " +
                "JOIN customers c ON l.account_number = c.account_number " +
                "WHERE c.branch = ?";  // Join customers table to get branch info

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, branch);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("loan_id"),
                        rs.getString("account_number"),
                        rs.getString("loan_type"),
                        rs.getDouble("amount"),
                        rs.getInt("duration_years"),
                        rs.getDouble("interest_rate"),
                        rs.getDouble("emi"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getString("status"),
                        rs.getDate("last_payment_date") != null ? rs.getDate("last_payment_date").toLocalDate() : null,
                        rs.getDouble("total_paid"),
                        rs.getDouble("fine")
                );
                loans.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    // Calculate EMI based on formula: [LoanAmount + 10% Interest] / (Years * 12 months)
    public static double calculateEMI(double loanAmount, int durationYears) {
        double totalWithInterest = loanAmount + (loanAmount * 0.10); // 10% interest
        return totalWithInterest / (durationYears * 12);
    }

    // Save loan request to database
    public static boolean saveLoan(Loan loan) {
        String query = "INSERT INTO loans (account_number, loan_type, amount, duration_years, interest_rate, emi, start_date, status, last_payment_date, total_paid, fine) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, loan.getAccountNumber());
            stmt.setString(2, loan.getLoanType());
            stmt.setDouble(3, loan.getAmount());
            stmt.setInt(4, loan.getDurationYears());
            stmt.setDouble(5, loan.getInterestRate());
            stmt.setDouble(6, loan.getEmi());
            stmt.setDate(7, Date.valueOf(loan.getStartDate()));
            stmt.setString(8, loan.getStatus());
            stmt.setDate(9, Date.valueOf(loan.getLastPaymentDate()));
            stmt.setDouble(10, loan.getTotalPaid());
            stmt.setDouble(11, loan.getFine());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Loan> getLoansByAccountNumber(String accountNumber) {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM loans WHERE account_number = ?";  // Query to fetch loans for a specific account number

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);  // Set account number parameter
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("loan_id"),
                        rs.getString("account_number"),
                        rs.getString("loan_type"),
                        rs.getDouble("amount"),
                        rs.getInt("duration_years"),
                        rs.getDouble("interest_rate"),
                        rs.getDouble("emi"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getString("status"),
                        rs.getDate("last_payment_date") != null ? rs.getDate("last_payment_date").toLocalDate() : null,
                        rs.getDouble("total_paid"),
                        rs.getDouble("fine")
                );
                loans.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;  
    }

    public static void processLoanPayments() {
        String query = "SELECT * FROM loans WHERE status = 'Active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int loanId = rs.getInt("loan_id");
                String accountNumber = rs.getString("account_number");
                double emi = rs.getDouble("emi");
                double totalPaid = rs.getDouble("total_paid");
                double loanAmount = rs.getDouble("amount");
                double fine = rs.getDouble("fine");
                Date lastPaymentDate = rs.getDate("last_payment_date");
                int missedMonths = rs.getInt("missed_months");

                // Convert lastPaymentDate to LocalDate
                LocalDate lastPayment = lastPaymentDate.toLocalDate();
                LocalDate today = LocalDate.now();

                // Calculate the number of days since the last payment
                long daysSinceLastPayment = ChronoUnit.DAYS.between(lastPayment, today);

                // Ensure deductions only happen if more than 30 days have passed
                if (daysSinceLastPayment > 30) {
                    // Calculate the number of missed months (exclude the first 30 days from the total)
                    int missedDays = (int) (daysSinceLastPayment - 30); // Exclude the first 30 days
                    missedMonths = Math.max(0, missedDays / 30); // Number of full missed months

                    // Calculate the fine if missed months are more than 0
                    if (missedMonths > 0) {
                        fine += missedMonths * 150; // Apply fine for each missed month
                    }

                    // Get the customer's balance to check if EMI can be deducted
                    String balanceQuery = "SELECT balance FROM customers WHERE account_number = ?";
                    double balance = 0.0;
                    try (PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery)) {
                        balanceStmt.setString(1, accountNumber);
                        ResultSet balanceRs = balanceStmt.executeQuery();
                        if (balanceRs.next()) {
                            balance = balanceRs.getDouble("balance");
                        }
                    }

                    // Deduct EMI if there is enough balance
                    boolean paymentProcessed = false;
                    if (balance >= emi) {
                        balance -= emi; // Deduct EMI from balance
                        totalPaid += emi; // Increase total paid
                        paymentProcessed = true;
                    } else {
                        // Deduct EMI even if balance is insufficient (debt incurred)
                        balance -= emi;
                        totalPaid += emi;
                        paymentProcessed = true;
                    }

                    // If payment was processed, update balance in the database
                    if (paymentProcessed) {
                        String updateBalance = "UPDATE customers SET balance = ? WHERE account_number = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateBalance)) {
                            updateStmt.setDouble(1, balance);
                            updateStmt.setString(2, accountNumber);
                            updateStmt.executeUpdate();
                        }
                    }

                    // Calculate the loan status based on total paid and loan amount
                    String loanStatus = (totalPaid >= loanAmount) ? "Completed" : "Active";
                    LocalDate newLastPaymentDate = (loanStatus.equals("Completed")) ? today : lastPayment;

                    // Update loan record with the new total paid, fine, and missed months
                    String updateLoan = "UPDATE loans SET total_paid = ?, fine = ?, status = ?, last_payment_date = ?, missed_months = ? WHERE loan_id = ?";
                    try (PreparedStatement updateLoanStmt = conn.prepareStatement(updateLoan)) {
                        updateLoanStmt.setDouble(1, totalPaid);
                        updateLoanStmt.setDouble(2, fine);
                        updateLoanStmt.setString(3, loanStatus);
                        updateLoanStmt.setDate(4, Date.valueOf(newLastPaymentDate));
                        updateLoanStmt.setInt(5, missedMonths);
                        updateLoanStmt.setInt(6, loanId);
                        updateLoanStmt.executeUpdate();
                    }

                    // If the loan is completed and there is any fine, deduct it from the customer's balance
                    if (loanStatus.equals("Completed") && fine > 0) {
                        String updateCustomerBalance = "UPDATE customers SET balance = ? WHERE account_number = ?";
                        try (PreparedStatement updateCustomerBalanceStmt = conn.prepareStatement(updateCustomerBalance)) {
                            balance -= fine;  // Deduct fine from customer's balance only when the loan is completed
                            updateCustomerBalanceStmt.setDouble(1, balance);
                            updateCustomerBalanceStmt.setString(2, accountNumber);
                            updateCustomerBalanceStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }













}
