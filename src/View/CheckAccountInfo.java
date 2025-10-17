package View;

import Controller.CustomerController;
import Model.Customer;

import javax.swing.*;
import java.awt.*;

public class CheckAccountInfo extends JFrame {
    private JLabel nameLabel, emailLabel, branchLabel, accNumLabel, balanceLabel, accountTypeLabel;;
    private JButton depositButton;
    private String accountNumber;

    public CheckAccountInfo(String accountNumber) {
        this.accountNumber = accountNumber;

        setTitle("Account Information");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE); // White background

        setLayout(new BorderLayout());

        // Header
        JLabel heading = new JLabel("Your Account Details", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(33, 64, 95));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(heading, BorderLayout.NORTH);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 20, 80)); // Padding all around

        Customer customer = CustomerController.getCustomerInfo(accountNumber);

        nameLabel = new JLabel("Name: " + customer.getName());
        emailLabel = new JLabel("Email: " + customer.getEmail());
        branchLabel = new JLabel("Branch: " + customer.getBranch());
        accNumLabel = new JLabel("Account Number: " + customer.getAccountNumber());
        balanceLabel = new JLabel("Account Balance: ₹" + customer.getBalance());
        accountTypeLabel = new JLabel("Account Type: " + customer.getAccountType());


        Font infoFont = new Font("Segoe UI", Font.PLAIN, 18);
        for (JLabel label : new JLabel[]{nameLabel, emailLabel, branchLabel, accNumLabel, balanceLabel, accountTypeLabel}) {
            label.setFont(infoFont);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Vertical spacing
            infoPanel.add(label);
        }


        // Deposit Button
        depositButton = new JButton("Deposit Money");
        depositButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        depositButton.setBackground(new Color(198, 230, 255)); // Soft pastel blue
        depositButton.setFocusPainted(false);
        depositButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        depositButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        depositButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        depositButton.setPreferredSize(new Dimension(180, 40));

        depositButton.addActionListener(e -> deposit());

        // Wrap in a sub-panel so it's not stuck at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 40, 10)); // Space around button
        buttonPanel.add(depositButton);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 30))); // gap before button
        infoPanel.add(buttonPanel);

        add(infoPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void deposit() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input.trim());
                if (amount <= 0) throw new NumberFormatException();

                boolean success = CustomerController.depositAmount(accountNumber, amount);
                if (success) {
                    CustomerController.logTransaction(accountNumber, "deposit", amount, null);
                    JOptionPane.showMessageDialog(this, "₹" + amount + " deposited successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Customer updated = CustomerController.getCustomerInfo(accountNumber);
                    balanceLabel.setText("Account Balance: ₹" + updated.getBalance());
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
