package View;

import Controller.BankStaffController;
import Model.BankStaff;
import Model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BankTransactionView extends JFrame {
    private BankStaff loggedInStaff;
    private BankStaffController bankStaffController;

    public BankTransactionView(BankStaff staff) {
        this.loggedInStaff = staff;
        this.bankStaffController = new BankStaffController();
        setTitle("Branch Transactions - " + staff.getBranch());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Transactions for Branch: " + staff.getBranch(), JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(heading, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Account Number", "Type", "Amount (₹)", "Related Account", "Timestamp"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch and populate transactions
        List<Transaction> transactions = bankStaffController.getBranchTransactions(loggedInStaff.getBranch());
        for (Transaction tx : transactions) {
            tableModel.addRow(new Object[]{
                    tx.getAccountNumber(),
                    tx.getType(),
                    String.format("₹%.2f", tx.getAmount()),
                    tx.getRelatedAccount() != null ? tx.getRelatedAccount() : "-",
                    tx.getTimestamp()
            });
        }

        setVisible(true);
    }
}

