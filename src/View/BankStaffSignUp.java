package View;

import Controller.BankStaffController;

import javax.swing.*;
import java.awt.*;

public class BankStaffSignUp extends JFrame {

    public BankStaffSignUp() {
        setTitle("Bank Staff Sign Up");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Bank Staff Signup");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 64, 95));

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        String[] branches = {"Bangalore", "Chennai", "Mumbai", "Mangalore"};
        JComboBox<String> branchDropdown = new JComboBox<>(branches);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(198, 230, 255));
        signupButton.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy++;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Branch:"), gbc);
        gbc.gridx = 1;
        add(branchDropdown, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(signupButton, gbc);

        signupButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String branch = (String) branchDropdown.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            BankStaffController controller = new BankStaffController();
            String bankUserNumber = controller.registerStaff(name, email, password, branch);

            if (bankUserNumber != null) {
                JOptionPane.showMessageDialog(this,
                        "Bank staff registered successfully!\nYour Bank User Number is: " + bankUserNumber,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // close the signup window
                new BankStaffLogin(); // 🔁 redirect to login
            } else {
                JOptionPane.showMessageDialog(this, "Error registering staff.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        setVisible(true);
    }
}
