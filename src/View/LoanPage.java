package View;

import Controller.LoanController;
import Model.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoanPage extends JFrame {
    private String accountNumber;

    public LoanPage(String accountNumber) {
        this.accountNumber = accountNumber;
        setTitle("Loan Information - Account: " + accountNumber);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        LoanController.processLoanPayments();

        JLabel title = new JLabel("Loans for Account: " + accountNumber, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 64, 95));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Updated column headers to include "Remaining Amount"
        String[] columns = {"Loan Type", "Total Loan (₹)", "EMI (₹)", "Remaining Amount (₹)", "Status", "Fine (₹)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable loanTable = new JTable(tableModel);
        add(new JScrollPane(loanTable), BorderLayout.CENTER);

        loadLoanData(tableModel);

        setVisible(true);
    }

    private void loadLoanData(DefaultTableModel tableModel) {
        List<Loan> loans = LoanController.getLoansByAccountNumber(accountNumber);

        for (Loan loan : loans) {
            double remaining = loan.getAmount() - loan.getTotalPaid();
            if (remaining < 0) remaining = 0;  // Prevent negative display if overpaid

            tableModel.addRow(new Object[]{
                    loan.getLoanType(),
                    "₹" + String.format("%.2f", loan.getAmount()),
                    "₹" + String.format("%.2f", loan.getEmi()),
                    "₹" + String.format("%.2f", remaining),
                    loan.getStatus(),
                    "₹" + String.format("%.2f", loan.getFine())
            });
        }
    }
}
