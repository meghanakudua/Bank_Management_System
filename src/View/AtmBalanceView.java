package View;

import Controller.BankStaffController;
import Model.BankStaff;

import javax.swing.*;
import java.awt.*;

public class AtmBalanceView extends JFrame {
    private JLabel balanceLabel;
    private JButton refillButton;

    public AtmBalanceView(BankStaff staff, BankStaffController controller) {
        setTitle("ATM Balance - " + staff.getBranch() + " Branch");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // UI Elements
        balanceLabel = new JLabel();
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        refillButton = new JButton("Refill ATM to ₹3,00,000");
        refillButton.setEnabled(false);
        refillButton.setVisible(false);

        // Fetch and show balance
        double balance = controller.getAtmBalance(staff.getBranch());
        balanceLabel.setText("ATM Balance: ₹" + String.format("%.2f", balance));

        if (balance < 50000) {
            refillButton.setEnabled(true);
            refillButton.setVisible(true);
        }

        refillButton.addActionListener(e -> {
            boolean success = controller.refillAtm(staff.getBranch());
            if (success) {
                JOptionPane.showMessageDialog(this, "ATM refilled successfully!");
                double updatedBalance = controller.getAtmBalance(staff.getBranch());
                balanceLabel.setText("ATM Balance: ₹" + String.format("%.2f", updatedBalance));
                refillButton.setEnabled(false);
                refillButton.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Refill failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(balanceLabel, BorderLayout.CENTER);
        add(refillButton, BorderLayout.SOUTH);
        setVisible(true);
    }
}
