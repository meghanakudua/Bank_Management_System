package View;

import Controller.BankStaffController;
import Model.BankStaff;

import javax.swing.*;
import java.awt.*;

public class BankStaffLogin extends JFrame {

    private JTextField userNumberField;
    private JPasswordField passwordField;

    public BankStaffLogin() {
        setTitle("Bank Staff Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Bank Staff Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        titleLabel.setForeground(new Color(33, 64, 95));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Bank User Number:"));
        userNumberField = new JTextField();
        formPanel.add(userNumberField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(198, 230, 255));
        loginBtn.setFocusPainted(false);
        formPanel.add(new JLabel()); // filler
        formPanel.add(loginBtn);

        add(formPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String userNum = userNumberField.getText().trim();
            String password = new String(passwordField.getPassword());

            BankStaffController controller = new BankStaffController();
            BankStaff staff = controller.login(userNum, password);

            if (staff != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new BankStaffHomePage(staff); // Show home page
            } else {
                JOptionPane.showMessageDialog(this, "Invalid user number or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
