package View;

import Controller.CustomerController;

import javax.swing.*;
import java.awt.*;

public class ATMPage extends JFrame {
    public ATMPage(String accountNumber) {
        setTitle("ATM - Withdraw Cash");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(20, 20));

        // Title
        JLabel titleLabel = new JLabel("ATM Withdrawal", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        titleLabel.setForeground(new Color(30, 60, 90));
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel amountLabel = new JLabel("Enter Amount to Withdraw:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220), 1, true));

        JLabel pinLabel = new JLabel("Enter ATM PIN:");
        pinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JPasswordField pinField = new JPasswordField(20);
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pinField.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220), 1, true));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(amountLabel, gbc);
        gbc.gridy++;
        formPanel.add(amountField, gbc);
        gbc.gridy++;
        formPanel.add(pinLabel, gbc);
        gbc.gridy++;
        formPanel.add(pinField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Withdraw Button
        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        withdrawBtn.setBackground(new Color(198, 230, 255));
        withdrawBtn.setForeground(Color.DARK_GRAY);
        withdrawBtn.setFocusPainted(false);
        withdrawBtn.setBorder(BorderFactory.createLineBorder(new Color(150, 180, 200), 1, true));
        withdrawBtn.setPreferredSize(new Dimension(120, 45));
        withdrawBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        buttonPanel.add(withdrawBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button Logic
        withdrawBtn.addActionListener(e -> {
            String amountText = amountField.getText().trim();
            String pinText = new String(pinField.getPassword()).trim();

            if (amountText.isEmpty() || pinText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isPinValid = CustomerController.validateLogin(accountNumber, pinText);
            if (!isPinValid) {
                JOptionPane.showMessageDialog(this, "Incorrect PIN.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            /*boolean success = CustomerController.withdrawAmount(accountNumber, amount);
            if (success) {
                CustomerController.logTransaction(accountNumber, "withdrawal", amount, null);
                JOptionPane.showMessageDialog(this, "₹" + amount + " withdrawn successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.WARNING_MESSAGE);
            }*/

            String result = CustomerController.withdrawAmount(accountNumber, amount);

            if ("SUCCESS".equals(result)) {
                CustomerController.logTransaction(accountNumber, "withdrawal", amount, null);
                JOptionPane.showMessageDialog(this, "₹" + amount + " withdrawn successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Withdrawal Error", JOptionPane.WARNING_MESSAGE);
            }

        });

        setVisible(true);
    }
}
