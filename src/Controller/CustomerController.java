package Controller;

import Model.Customer;
import Model.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerController {

    public static String[] processCustomerSignup(String name, String email, String branch,
                                                 String accountType, String employmentStatus, double monthlySalary) {
        String accountNumber = generateUniqueAccountNumber();
        String cardNumber = generateUniqueATMCardNumber();
        String pin = generateUniquePin();

        Customer customer = new Customer(name, email, accountNumber, cardNumber, pin, branch,
                accountType, employmentStatus, monthlySalary);

        if (saveCustomerToDatabase(customer)) {
            return new String[]{accountNumber, cardNumber, pin};
        } else {
            return new String[]{"Error", "Error", "Error"};
        }
    }


    private static boolean saveCustomerToDatabase(Customer customer) {
        String query = "INSERT INTO customers (name, email, account_number, card_number, pin, branch, " +
                "account_type, employment_status, monthly_salary, balance, last_salary_credit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getAccountNumber());
            stmt.setString(4, customer.getCardNumber());
            stmt.setString(5, customer.getPin());
            stmt.setString(6, customer.getBranch());
            stmt.setString(7, customer.getAccountType());
            stmt.setString(8, customer.getEmploymentStatus());
            stmt.setDouble(9, customer.getMonthlySalary());
            stmt.setInt(10, 500); // Initial balance

            // Set initial last_salary_credit to 2025-03-10
            java.sql.Date initialCreditDate = java.sql.Date.valueOf("2025-03-10");
            stmt.setDate(11, initialCreditDate);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static void creditMonthlySalaryIfDue(String accountNumber) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT monthly_salary, employment_status, last_salary_credit FROM customers WHERE account_number = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int salary = rs.getInt("monthly_salary");
                String status = rs.getString("employment_status");
                Date lastCreditDate = rs.getDate("last_salary_credit");

                LocalDate today = LocalDate.now();
                boolean shouldCredit = false;

                if ("Employed".equalsIgnoreCase(status)) {
                    if (lastCreditDate == null || lastCreditDate.toLocalDate().getMonthValue() < today.getMonthValue()
                            || lastCreditDate.toLocalDate().getYear() < today.getYear()) {
                        shouldCredit = true;
                    }
                }

                if (shouldCredit && salary > 0) {
                    PreparedStatement update = conn.prepareStatement(
                            "UPDATE customers SET balance = balance + ?, last_salary_credit = ? WHERE account_number = ?");
                    update.setInt(1, salary);
                    update.setDate(2, Date.valueOf(today));
                    update.setString(3, accountNumber);
                    update.executeUpdate();

                    PreparedStatement log = conn.prepareStatement(
                            "INSERT INTO transactions (account_number, type, amount, timestamp) VALUES (?, 'salary_credit', ?, NOW())");
                    log.setString(1, accountNumber);
                    log.setInt(2, salary);
                    log.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static String generateUniqueAccountNumber() {
        String accNum;
        do {
            int number = new Random().nextInt(900) + 100; // 100–999
            accNum = "CU" + number;
        } while (valueExistsInDatabase("account_number", accNum));
        return accNum;
    }

    private static String generateUniqueATMCardNumber() {
        String card;
        do {
            StringBuilder sb = new StringBuilder();
            Random r = new Random();
            for (int i = 0; i < 12; i++) {
                sb.append(r.nextInt(10));
            }
            card = sb.toString();
        } while (valueExistsInDatabase("card_number", card));
        return card;
    }

    private static String generateUniquePin() {
        String pin;
        do {
            int pinNum = new Random().nextInt(9000) + 1000;
            pin = String.valueOf(pinNum);
        } while (valueExistsInDatabase("pin", pin));
        return pin;
    }

    private static boolean valueExistsInDatabase(String columnName, String value) {
        String query = "SELECT COUNT(*) FROM customers WHERE " + columnName + " = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; // Just in case of an error, assume it exists to be safe
    }

    public static boolean validateLogin(String accountNumber, String pin) {
        String query = "SELECT * FROM customers WHERE account_number = ? AND pin = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            stmt.setString(2, pin);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // If login is successful, credit salary if due
                creditMonthlySalaryIfDue(accountNumber);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getCustomerName(String accountNumber) {
        String query = "SELECT name FROM customers WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Customer";
    }

    // Fetch customer info
    public static Customer getCustomerInfo(String accountNumber) {
        String query = "SELECT account_number, card_number, pin, branch, name, email, balance, employment_status, account_type, monthly_salary FROM customers WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getString("account_number"),
                        rs.getString("card_number"),
                        rs.getString("pin"),
                        rs.getString("branch"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDouble("balance"),
                        rs.getString("employment_status"),
                        rs.getString("account_type"),
                        rs.getDouble("monthly_salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Update balance
    public static boolean depositAmount(String accountNumber, double amount) {
        String query = "UPDATE customers SET balance = balance + ? WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String withdrawAmount(String accountNumber, double amount) {
        String getCustomerQuery = "SELECT balance, branch FROM customers WHERE account_number = ?";
        String getAtmBalanceQuery = "SELECT balance FROM atm_balance WHERE branch = ?";
        String sumWithdrawalsQuery = "SELECT SUM(amount) AS total FROM transactions WHERE account_number = ? AND type = 'withdrawal' AND DATE(timestamp) = CURDATE()";
        String updateCustomerQuery = "UPDATE customers SET balance = balance - ? WHERE account_number = ?";
        String updateAtmQuery = "UPDATE atm_balance SET balance = balance - ? WHERE branch = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getCustomerStmt = conn.prepareStatement(getCustomerQuery);
             PreparedStatement getAtmStmt = conn.prepareStatement(getAtmBalanceQuery);
             PreparedStatement sumWithdrawalsStmt = conn.prepareStatement(sumWithdrawalsQuery);
             PreparedStatement updateCustomerStmt = conn.prepareStatement(updateCustomerQuery);
             PreparedStatement updateAtmStmt = conn.prepareStatement(updateAtmQuery)) {

            getCustomerStmt.setString(1, accountNumber);
            ResultSet rs = getCustomerStmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                String branch = rs.getString("branch");

                // Get current ATM balance
                getAtmStmt.setString(1, branch);
                ResultSet atmRs = getAtmStmt.executeQuery();
                if (!atmRs.next()) return "ATM not found.";

                double atmBalance = atmRs.getDouble("balance");

                // Check if ATM has enough funds
                if (atmBalance < 50000) return "ATM balance is below ₹50,000. Cannot withdraw at this time.";
                if (atmBalance < amount) return "ATM doesn't have enough cash for this transaction.";

                // Check daily withdrawal limit
                sumWithdrawalsStmt.setString(1, accountNumber);
                ResultSet sumRs = sumWithdrawalsStmt.executeQuery();
                double totalWithdrawnToday = 0;
                if (sumRs.next()) {
                    totalWithdrawnToday = sumRs.getDouble("total");
                }

                if ((totalWithdrawnToday + amount) > 50000) {
                    return "Withdrawal limit of ₹50,000 per day exceeded.";
                }

                if (balance >= amount) {
                    updateCustomerStmt.setDouble(1, amount);
                    updateCustomerStmt.setString(2, accountNumber);
                    int customerUpdated = updateCustomerStmt.executeUpdate();

                    updateAtmStmt.setDouble(1, amount);
                    updateAtmStmt.setString(2, branch);
                    int atmUpdated = updateAtmStmt.executeUpdate();

                    if (customerUpdated > 0 && atmUpdated > 0) return "SUCCESS";
                } else {
                    return "Insufficient account balance.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Withdrawal failed. Please try again.";
    }





    public static String transferMoney(String senderAcc, String recipientAcc, double amount, String pin) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT account_number, pin, balance, branch FROM customers WHERE account_number=?");
            ps.setString(1, senderAcc);
            ResultSet rsSender = ps.executeQuery();

            if (!rsSender.next()) return "Sender account not found.";

            String correctPin = rsSender.getString("pin");
            double senderBalance = rsSender.getDouble("balance");
            String senderBranch = rsSender.getString("branch");

            if (!correctPin.equals(pin)) return "Incorrect PIN.";
            if (senderAcc.equals(recipientAcc)) return "Cannot transfer to the same account.";

            PreparedStatement ps2 = conn.prepareStatement("SELECT balance, branch FROM customers WHERE account_number=?");
            ps2.setString(1, recipientAcc);
            ResultSet rsRecipient = ps2.executeQuery();

            if (!rsRecipient.next()) return "Recipient account not found.";

            String recipientBranch = rsRecipient.getString("branch");
            double recipientBalance = rsRecipient.getDouble("balance");

            double charge = senderBranch.equals(recipientBranch) ? 0 : 50;
            double totalDeduct = amount + charge;

            if (senderBalance < totalDeduct) return "Insufficient balance. ₹" + charge + " branch charge applies.";

            // Proceed with transfer
            PreparedStatement updateSender = conn.prepareStatement("UPDATE customers SET balance=? WHERE account_number=?");
            updateSender.setDouble(1, senderBalance - totalDeduct);
            updateSender.setString(2, senderAcc);
            updateSender.executeUpdate();

            PreparedStatement updateRecipient = conn.prepareStatement("UPDATE customers SET balance=? WHERE account_number=?");
            updateRecipient.setDouble(1, recipientBalance + amount);
            updateRecipient.setString(2, recipientAcc);
            updateRecipient.executeUpdate();

            // Log transactions
            logTransaction(senderAcc, "account transfer", amount, "to " + recipientAcc);
            logTransaction(recipientAcc, "account transfer", amount, "from " + senderAcc);

            //conn.commit();

            return "Transfer successful! ₹" + amount + " sent to " + recipientAcc +
                    (charge > 0 ? " with ₹50 inter-branch charge." : ".");
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred during transfer.";
        }
    }

    public static void logTransaction(String accountNumber, String type, double amount, String relatedAccount) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO transactions (account_number, type, amount, related_account) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, accountNumber);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setString(4, relatedAccount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // In CustomerController.java
    public List<Customer> getCustomersByBranch(String branch) {
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE branch = ?");
            stmt.setString(1, branch);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getString("account_number"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("branch"),
                        rs.getDouble("balance")
                );
                customers.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }









}
