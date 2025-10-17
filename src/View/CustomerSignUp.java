package View;

import Controller.CustomerController;

import javax.swing.*;
import java.awt.*;

public class CustomerSignUp extends JFrame {
    private JTextField nameField, emailField, salaryField;
    private JComboBox<String> branchSelector, employmentStatusSelector, accountTypeSelector;
    private JButton payButton;

    public CustomerSignUp() {
        setTitle("Customer Signup");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Customer Signup", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Name Field
        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        // Email Field
        formPanel.add(new JLabel("Email ID:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        // Branch Selector
        formPanel.add(new JLabel("Select Branch:"));
        branchSelector = new JComboBox<>(new String[]{"Select Branch", "Bangalore", "Chennai", "Mumbai", "Mangalore"});
        formPanel.add(branchSelector);

        // Employment Status
        formPanel.add(new JLabel("Employment Status:"));
        employmentStatusSelector = new JComboBox<>(new String[]{"Select", "Employed", "Unemployed"});
        formPanel.add(employmentStatusSelector);

        // Account Type
        formPanel.add(new JLabel("Account Type:"));
        accountTypeSelector = new JComboBox<>(new String[]{"Salary", "Savings"});
        formPanel.add(accountTypeSelector);

        // Monthly Salary
        formPanel.add(new JLabel("Monthly Salary (₹):"));
        salaryField = new JTextField();
        formPanel.add(salaryField);

        add(formPanel, BorderLayout.CENTER);

        // Visibility logic
        accountTypeSelector.setVisible(false);
        salaryField.setVisible(false);

        employmentStatusSelector.addActionListener(e -> {
            String status = (String) employmentStatusSelector.getSelectedItem();
            boolean employed = "Employed".equals(status);
            accountTypeSelector.setVisible(employed);
            salaryField.setVisible(employed);
            revalidate();
            repaint();
        });

        // Pay Button
        payButton = new JButton("Proceed to Pay ₹500 Safety Deposit");
        payButton.setBackground(new Color(173, 216, 230));
        payButton.setFocusPainted(false);
        payButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        payButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        payButton.setPreferredSize(new Dimension(100, 40));
        add(payButton, BorderLayout.SOUTH);

        payButton.addActionListener(e -> handlePayment());

        setVisible(true);
    }

    private void handlePayment() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String selectedBranch = (String) branchSelector.getSelectedItem();
        String employmentStatus = (String) employmentStatusSelector.getSelectedItem();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name and email.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedBranch == null || selectedBranch.equals("Select Branch")) {
            JOptionPane.showMessageDialog(this, "Please select a valid branch.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (employmentStatus == null || employmentStatus.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select employment status.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String accountType = "Savings";
        double monthlySalary = 0;

        if (employmentStatus.equals("Employed")) {
            accountType = (String) accountTypeSelector.getSelectedItem();
            if (accountType == null) {
                JOptionPane.showMessageDialog(this, "Please select account type.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                monthlySalary = Double.parseDouble(salaryField.getText().trim());
                if (monthlySalary <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive salary.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Pay ₹500 safety deposit?", "Payment Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            String[] generated = CustomerController.processCustomerSignup(
                    name, email, selectedBranch, employmentStatus, accountType, monthlySalary
            );

            if (!generated[0].equals("Error")) {
                JOptionPane.showMessageDialog(this,
                        "Account Created Successfully!\n" +
                                "Account Number: " + generated[0] + "\n" +
                                "ATM Card Number: " + generated[1] + "\n" +
                                "ATM PIN: " + generated[2],
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                SwingUtilities.invokeLater(() -> new CustomerLogin());
            } else {
                JOptionPane.showMessageDialog(this, "There was an error processing your signup.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            int retry = JOptionPane.showConfirmDialog(this,
                    "To continue using your bank account and generate an account number, ATM PIN, and debit card, please pay ₹500 as a safety deposit.\n\nWould you like to pay now?",
                    "Payment Required", JOptionPane.YES_NO_OPTION);

            if (retry == JOptionPane.YES_OPTION) {
                handlePayment();
            } else {
                JOptionPane.showMessageDialog(this, "Signup cancelled.", "Cancelled", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
