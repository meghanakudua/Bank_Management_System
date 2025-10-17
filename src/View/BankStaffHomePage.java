package View;

import Controller.BankStaffController;
import Model.BankStaff;

import javax.swing.*;
import java.awt.*;

public class BankStaffHomePage extends JFrame {

    public BankStaffHomePage(BankStaff staff) {
        setTitle("Bank Staff Dashboard");
        setSize(600, 500); // Bigger size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🌟 Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + staff.getName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(welcomeLabel, gbc);

        // 🔘 Buttons
        String[] buttonTexts = {
                "My Account",
                "Transactions",
                "Customers Info",
                "Loan Approval & View All Loans",
                "Check ATM Balance"
        };

        JButton[] buttons = new JButton[buttonTexts.length];

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < buttonTexts.length; i++) {
            buttons[i] = new JButton(buttonTexts[i]);
            buttons[i].setFocusPainted(false);
            buttons[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            buttons[i].setPreferredSize(new Dimension(200, 40));
            buttons[i].setBackground(new Color(173, 216, 230));
            buttons[i].setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true));
            gbc.gridy = i + 1; // Shift down because of welcome message
            add(buttons[i], gbc);
        }

        // Action for "My Account"
        buttons[0].addActionListener(e -> new BankStaffAccountInfoPage(staff));
        // Inside the loop or after creating buttons
        buttons[2].addActionListener(e -> new CustomerInfoPage(staff));
        buttons[1].addActionListener(e -> new BankTransactionView(staff));
        buttons[4].addActionListener(e -> new AtmBalanceView(staff, new BankStaffController()));
        buttons[3].addActionListener(e -> new ViewAllLoansView(staff));





        setVisible(true);
    }
}
