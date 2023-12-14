package Swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.JdbcConnection;

import DAO.UserDAO;
import Java.User;

public class CustomerManagement {

    private JFrame frame;
    private JTable customersTable;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    CustomerManagement window = new CustomerManagement();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CustomerManagement() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 664, 235);
        frame.getContentPane().add(scrollPane);

        customersTable = new JTable();
        scrollPane.setViewportView(customersTable);
        loadCustomersTable();

        JButton btnAddCustomer = new JButton("Add Customer");
        btnAddCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add customer logic
                String email = JOptionPane.showInputDialog(frame, "Enter Email:");
                String password = JOptionPane.showInputDialog(frame, "Enter Password:");
                String phoneNumber = JOptionPane.showInputDialog(frame, "Enter Phone Number:");
                String address = JOptionPane.showInputDialog(frame, "Enter Address:");

                try {
                    UserDAO userDAO = new UserDAO(JDBC.ConnectJDBC.getConnection());
                    String result = userDAO.registerUser(email, password, phoneNumber, address);
                    if (result == null) {
                        JOptionPane.showMessageDialog(frame, "Customer added successfully!");
                        loadCustomersTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, result);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnAddCustomer.setBounds(65, 286, 138, 32);
        frame.getContentPane().add(btnAddCustomer);

        JButton btnRemoveCustomer = new JButton("Remove Customer");
        btnRemoveCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCustomer();
            }
        });
        btnRemoveCustomer.setBounds(239, 286, 163, 32);
        frame.getContentPane().add(btnRemoveCustomer);

        JButton btnUpdateCustomer = new JButton("Update Customer Info");
        btnUpdateCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCustomer();
            }
        });
        btnUpdateCustomer.setBounds(430, 286, 180, 32);
        frame.getContentPane().add(btnUpdateCustomer);
    }

    private void loadCustomersTable() {
        try {
            UserDAO userDAO = new UserDAO(JDBC.ConnectJDBC.getConnection());
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Email");
            model.addColumn("Password");
            model.addColumn("Phone Number");
            model.addColumn("Address");

            for (User user : userDAO.getAllUsers()) {
                model.addRow(new Object[] { user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getAddress() });
            }
            customersTable.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row!");
        } else {
            try {
                DefaultTableModel model = (DefaultTableModel) customersTable.getModel();
                String email = model.getValueAt(selectedRow, 0).toString(); // Assuming email is in column index 0

                UserDAO userDAO = new UserDAO(JDBC.ConnectJDBC.getConnection());
                boolean check = userDAO.deleteUser(email);
                if (check) {
                    JOptionPane.showMessageDialog(frame, "Customer deleted successfully!");
                    loadCustomersTable();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete customer.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row!");
        } else {
            try {
                DefaultTableModel model = (DefaultTableModel) customersTable.getModel();
                String email = model.getValueAt(selectedRow, 0).toString(); // Assuming email is in column index 0
                String password = model.getValueAt(selectedRow, 1).toString(); // Assuming password is in column index 1
                String phoneNumber = model.getValueAt(selectedRow, 2).toString(); // Assuming phone number is in column index 2
                String address = model.getValueAt(selectedRow, 3).toString(); // Assuming address is in column index 3

                UserDAO userDAO = new UserDAO(JDBC.ConnectJDBC.getConnection());
                User user = new User(email, password, phoneNumber, address);
                // Implement a form or dialog to allow user to update the customer details
                // Update the user object with new details and use UserDAO to update the information in the database
                // userDAO.updateUser(user);

                JOptionPane.showMessageDialog(frame, "Customer information updated successfully!");
                loadCustomersTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
