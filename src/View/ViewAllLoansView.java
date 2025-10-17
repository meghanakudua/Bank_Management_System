package View;

import Controller.LoanController;
import Model.Loan;
import Model.BankStaff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewAllLoansView extends JFrame {

    private BankStaff staff;

    public ViewAllLoansView(BankStaff staff) {
        this.staff = staff;

        // Extract branch from the BankStaff object
        String staffBranch = staff.getBranch();

        setTitle("All Loans - Branch: " + staffBranch);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Loans in Branch: " + staffBranch, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 64, 95));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Account Number", "Loan Type", "Amount", "Duration (Years)", "EMI (₹)", "Status", "Fine (₹)", "Total Paid (₹)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadLoans(tableModel);

        setVisible(true);
    }

    private void loadLoans(DefaultTableModel tableModel) {
        List<Loan> loans = LoanController.getAllLoansForBranch(staff.getBranch());

        for (Loan loan : loans) {
            tableModel.addRow(new Object[]{
                    loan.getAccountNumber(),
                    loan.getLoanType(),
                    "₹" + loan.getAmount(),
                    loan.getDurationYears(),
                    "₹" + String.format("%.2f", loan.getEmi()),
                    loan.getStatus(),
                    "₹" + loan.getFine(),
                    "₹" + loan.getTotalPaid()
            });
        }
    }
}
