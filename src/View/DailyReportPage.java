package View;

import Controller.CustomerController;
import Model.DBConnection;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyReportPage extends JFrame {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JDateChooser dateChooser;
    private String accountNumber;

    public DailyReportPage(String accountNumber) {
        this.accountNumber = accountNumber;

        setTitle("Daily Transaction Report");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Your Daily Transaction Report", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        titleLabel.setForeground(new Color(33, 64, 95));
        add(titleLabel, BorderLayout.NORTH);

        // Top panel for date selection
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        JButton showReportButton = new JButton("Show Report");
        showReportButton.setFocusPainted(false);
        showReportButton.setBackground(new Color(198, 230, 255));

        topPanel.add(new JLabel("Select Date:"));
        topPanel.add(dateChooser);
        topPanel.add(showReportButton);
        add(topPanel, BorderLayout.CENTER);

        // Table for report
        tableModel = new DefaultTableModel(new Object[]{"Type", "Time", "Amount", "Related Account"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(scrollPane, BorderLayout.SOUTH);

        showReportButton.addActionListener(e -> loadReport());

        setVisible(true);
    }

    private void loadReport() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0); // Clear old data

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT type, timestamp, amount, related_account FROM transactions " +
                    "WHERE account_number = ? AND DATE(timestamp) = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, accountNumber);
            stmt.setString(2, sdf.format(selectedDate));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                double amount = rs.getDouble("amount");
                String related = rs.getString("related_account");

                if (type.equalsIgnoreCase("account transfer")) {
                    if (accountNumber.equals(related)) {
                        type = "Transfer From Another Account";
                    } else {
                        type = "Transfer To Another Account";
                    }
                }

                tableModel.addRow(new Object[]{type, timestamp.toString(), "₹" + amount, related});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
