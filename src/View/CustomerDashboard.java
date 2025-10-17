package View;

import Controller.CustomerController;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerDashboard extends JFrame {

    public CustomerDashboard(String accountNumber) {
        String customerName = CustomerController.getCustomerName(accountNumber);

        setTitle("Customer Dashboard - " + customerName);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 250, 255)); // Light pleasant background
        setLayout(new BorderLayout());

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome to XYZ Bank, " + customerName + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        welcomeLabel.setForeground(new Color(33, 64, 95)); // Soft navy blue
        add(welcomeLabel, BorderLayout.NORTH);

        // Panel for Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 25, 25)); // Change grid to 6 buttons
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 250, 40, 250)); // Margin on sides
        buttonPanel.setBackground(new Color(245, 250, 255)); // Match background

        String[] labels = {
                "ATM",
                "Loan",
                "Check Account Info",
                "Generate Daily Report",
                "Transactions",
                "View Active Loans"  // New button for viewing active loans
        };

        Color buttonColor = new Color(198, 230, 255); // Soft blue pastel

        for (String label : labels) {
            JButton btn = new JButton(label);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            btn.setBackground(buttonColor);
            btn.setForeground(Color.DARK_GRAY);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new RoundedBorder(20));
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);

            // Add specific action for "Check Account Info"
            if (label.equals("Check Account Info")) {
                btn.addActionListener(e -> new CheckAccountInfo(accountNumber));
            }

            if (label.equals("ATM")) {
                btn.addActionListener(e -> new ATMPage(accountNumber));
            }

            if (label.equals("Transactions")) {
                btn.addActionListener(e -> new TransactionPage(accountNumber));
            }

            if (label.equals("Generate Daily Report")) {
                btn.addActionListener(e -> new DailyReportPage(accountNumber));
            }

            // Add specific action for "Loan"
            if (label.equals("Loan")) {
                btn.addActionListener(e -> new LoanRequestView(accountNumber));  // Change LoanPage to the actual page you're using
            }

            // Add specific action for "View Active Loans"
            if (label.equals("View Active Loans")) {
                btn.addActionListener(e -> new LoanPage(accountNumber));  // Open LoanPage for the customer
            }

            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Rounded border class
    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(180, 200, 220)); // outline color
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 2, radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = radius + 1;
            insets.top = radius + 1;
            insets.right = radius;
            insets.bottom = radius + 2;
            return insets;
        }
    }
}
