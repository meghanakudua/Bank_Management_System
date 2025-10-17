package View;

import Controller.CustomerController;

import javax.swing.*;
import java.awt.*;

public class CustomerLogin extends JFrame {
    private JTextField accountNumberField;
    private JPasswordField pinField;
    private JButton loginButton;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Customer Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        formPanel.add(new JLabel("Account Number:"));
        accountNumberField = new JTextField();
        formPanel.add(accountNumberField);

        formPanel.add(new JLabel("ATM PIN:"));
        pinField = new JPasswordField();
        formPanel.add(pinField);

        loginButton = new JButton("Login");
        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    private void handleLogin() {
        String accountNumber = accountNumberField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (CustomerController.validateLogin(accountNumber, pin)) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close login screen
            new CustomerDashboard(accountNumber); // Pass account number to dashboard
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
