package View;

import Controller.CustomerController;
import Controller.LoanController;
import Model.Customer;
import Model.Loan;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class LoanRequestView extends JFrame {

    private String accountNumber;
    private JComboBox<String> loanTypeDropdown;
    private JTextField amountField, durationField;
    private JButton requestLoanButton;

    public LoanRequestView(String accountNumber) {
        this.accountNumber = accountNumber;

        setTitle("Loan Request");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Apply for Loan", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 64, 95));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(Color.WHITE);

        // Loan Type
        formPanel.add(new JLabel("Loan Type:"));
        loanTypeDropdown = new JComboBox<>(new String[]{"Car", "House", "Personal"});
        formPanel.add(loanTypeDropdown);

        // Amount
        formPanel.add(new JLabel("Loan Amount (₹):"));
        amountField = new JTextField();
        formPanel.add(amountField);

        // Duration
        formPanel.add(new JLabel("Duration (Years):"));
        durationField = new JTextField();
        formPanel.add(durationField);

        add(formPanel, BorderLayout.CENTER);

        // Button
        requestLoanButton = new JButton("Submit Loan Request");
        requestLoanButton.setBackground(new Color(198, 230, 255));
        requestLoanButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        requestLoanButton.setFocusPainted(false);
        requestLoanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        requestLoanButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        buttonPanel.add(requestLoanButton);
        add(buttonPanel, BorderLayout.SOUTH);

        requestLoanButton.addActionListener(e -> handleLoanRequest());

        setVisible(true);
    }

    private void handleLoanRequest() {
        Customer customer = CustomerController.getCustomerInfo(accountNumber);

        if (!LoanController.isEligibleForLoan(customer)) {
            JOptionPane.showMessageDialog(this, "Sorry, you are not eligible for a loan.\n(Employed & Salary ≥ ₹2,00,000 required)", "Ineligible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (LoanController.hasActiveLoan(accountNumber)) {
            JOptionPane.showMessageDialog(this, "You already have an active loan.\nComplete it before applying for a new one.", "Active Loan Exists", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String loanType = loanTypeDropdown.getSelectedItem().toString();
        double amount;
        int durationYears;

        try {
            amount = Double.parseDouble(amountField.getText().trim());
            durationYears = Integer.parseInt(durationField.getText().trim());

            if (amount <= 0 || durationYears <= 0) throw new NumberFormatException();

            double emi = LoanController.calculateEMI(amount, durationYears);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Loan Amount: ₹" + amount +
                            "\nDuration: " + durationYears + " years" +
                            "\nMonthly EMI: ₹" + String.format("%.2f", emi) +
                            "\n\nConfirm loan request?",
                    "Confirm Loan Request",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Loan loan = new Loan(
                        accountNumber,
                        loanType,
                        amount,
                        durationYears,
                        emi // Corrected to match the constructor
                );

                boolean success = LoanController.saveLoan(loan);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Loan request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Something went wrong while processing the loan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for amount and duration.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

}
