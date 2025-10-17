package View;

import Controller.CustomerController;

import javax.swing.*;
import java.awt.*;

public class TransactionPage extends JFrame {
    public TransactionPage(String senderAccountNumber) {
        setTitle("Fund Transfer");
        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Transfer Money", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));
        panel.setBackground(Color.WHITE);

        JTextField recipientField = new JTextField();
        JTextField amountField = new JTextField();
        JPasswordField pinField = new JPasswordField();

        panel.add(new JLabel("Recipient Account No:"));
        panel.add(recipientField);
        panel.add(new JLabel("Amount to Transfer:"));
        panel.add(amountField);
        panel.add(new JLabel("Enter ATM PIN:"));
        panel.add(pinField);

        add(panel, BorderLayout.CENTER);

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBackground(new Color(198, 230, 255));
        transferBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        transferBtn.setFocusPainted(false);
        transferBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(transferBtn);
        add(btnPanel, BorderLayout.SOUTH);

        transferBtn.addActionListener(e -> {
            String recipient = recipientField.getText().trim();
            String amountText = amountField.getText().trim();
            String pin = new String(pinField.getPassword());

            if (recipient.isEmpty() || amountText.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);

                String result = CustomerController.transferMoney(senderAccountNumber, recipient, amount, pin);
                JOptionPane.showMessageDialog(this, result);
                if (result.contains("successful")) {
                    dispose(); // close window
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
