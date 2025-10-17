package View;

import Controller.CustomerController;
import Model.BankStaff;
import Model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerInfoPage extends JFrame {

    public CustomerInfoPage(BankStaff staff) {
        setTitle("Customers Info - " + staff.getBranch() + " Branch");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Name", "Email", "Account Number", "Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        CustomerController controller = new CustomerController();
        List<Customer> customers = controller.getCustomersByBranch(staff.getBranch());

        for (Customer c : customers) {
            Object[] row = {
                    c.getName(),
                    c.getEmail(),
                    c.getAccountNumber(),
                    "₹" + String.format("%.2f", c.getBalance())
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        setVisible(true);
    }
}
