package View;

import Model.BankStaff;

import javax.swing.*;
import java.awt.*;

public class BankStaffAccountInfoPage extends JFrame {

    private BankStaff staff;

    public BankStaffAccountInfoPage(BankStaff staff) {
        this.staff = staff;

        setTitle("My Account Info");
        setSize(500, 400); // ⬅️ Bigger size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Name:");
        JLabel userNumberLabel = new JLabel("Bank User Number:");
        JLabel emailLabel = new JLabel("Email ID:");
        JLabel branchLabel = new JLabel("Branch:");

        JLabel nameValue = new JLabel(staff.getName());
        JLabel userNumberValue = new JLabel(staff.getBankUserNumber());
        JLabel emailValue = new JLabel(staff.getEmail());
        JLabel branchValue = new JLabel(staff.getBranch());

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel[] labels = {nameLabel, userNumberLabel, emailLabel, branchLabel};
        JLabel[] values = {nameValue, userNumberValue, emailValue, branchValue};

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(labelFont);
            values[i].setFont(valueFont);

            gbc.gridy = i;
            gbc.gridx = 0;
            add(labels[i], gbc);

            gbc.gridx = 1;
            add(values[i], gbc);
        }

        setVisible(true);
    }
}
