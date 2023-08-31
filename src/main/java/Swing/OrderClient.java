package Swing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import DAO.OrderDAO;
import DAO.ProductDAO;
import DAO.UserDAO;
import Java.Order;
import Java.Product;
import Java.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;




public class OrderClient {

    private JFrame frame;
    private JTable ordersTable;
    private OrderDAO orderDao;

    private List<Order> previousOrders;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Set the look and feel to the system's default
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    OrderClient window = new OrderClient();
                    window.frame.setVisible(true);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public OrderClient() throws ClassNotFoundException, SQLException {
        initialize();
        orderDao = new OrderDAO(JDBC.ConnectJDBC.getConnection());
        updateTable(ordersTable, orderDao);
        
        JButton Productmanamentbutton = new JButton("Product Manament");
        Productmanamentbutton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ProductManament prdmn;
				try {
					prdmn = new ProductManament();
					prdmn.frame.setVisible(true);
				} catch (Exception e1) {
				
					e1.printStackTrace();
				} 
        		
        	}
        });
        Productmanamentbutton.setForeground(Color.BLACK);
        Productmanamentbutton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        Productmanamentbutton.setBackground(new Color(102, 187, 106));
        Productmanamentbutton.setBounds(30, 380, 238, 36);
        frame.getContentPane().add(Productmanamentbutton);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 921, 478);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 58, 848, 213);
        frame.getContentPane().add(scrollPane);

        ordersTable = new JTable();
        ordersTable.setModel(
                new DefaultTableModel(
                    new Object[][] { { null, null, null, null, null }, },
                    new String[] { "Order Date", "Order Name", "Order Quantity", "Customer Mail", "Customer address" })
        );
        ordersTable.getColumnModel().getColumn(2).setPreferredWidth(111);
        ordersTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        ordersTable.getColumnModel().getColumn(4).setPreferredWidth(237);
        scrollPane.setViewportView(ordersTable);
        decorateTable();

        JButton updateButton = new JButton("Update Change");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a row!");
                } else {
                    try {
                        DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
                        Order order = new Order();
                        order.setDate(model.getValueAt(selectedRow, 0).toString());
                        order.setOrderName(model.getValueAt(selectedRow, 1).toString());
                        order.setQuantity(Integer.parseInt((String) model.getValueAt(selectedRow, 2)));
                        order.setUid(model.getValueAt(selectedRow, 3).toString());
                        boolean update = orderDao.updateOrder(order);
                        if (update) {
                            JOptionPane.showMessageDialog(frame, "Update succeed");
                            updateTable(ordersTable, orderDao);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Something went wrong");
                            updateTable(ordersTable, orderDao);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        updateButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        updateButton.setBounds(703, 285, 169, 36);
        updateButton.setBackground(new Color(41, 182, 246)); // Set button background color
        updateButton.setForeground(Color.BLACK); // Set button text color
        frame.getContentPane().add(updateButton);

        JButton deleteButton = new JButton("Delete Orders");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a row!");
                } else {
                    DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
                    String name = (String) model.getValueAt(selectedRow, 1);
                    boolean check = orderDao.cancelOrder(name);
                    if (check) {
                        JOptionPane.showMessageDialog(frame, "Delete Order Done!");
                        updateTable(ordersTable, orderDao);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Something went wrong!");
                        updateTable(ordersTable, orderDao);
                    }
                }
            }
        });
        deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        deleteButton.setBounds(30, 281, 238, 36);
        deleteButton.setBackground(new Color(239, 83, 80)); // Set button background color
        deleteButton.setForeground(Color.BLACK); // Set button text color
        frame.getContentPane().add(deleteButton);

        JButton markDoneButton = new JButton("Mark Done!");
        markDoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a row!");
                } else {
                    try {
                        ProductDAO pd = new ProductDAO(JDBC.ConnectJDBC.getConnection());
                        int stock = Integer.parseInt(model.getValueAt(selectedRow, 2).toString());
                        Product product = pd.getProductByName(model.getValueAt(selectedRow, 1).toString());
                        int newStock = product.getStock() - stock;
                        if (newStock < 0) {
                            JOptionPane.showMessageDialog(frame, "The order quantity is greater than stock!");
                        } else {
                            product.setStock(newStock);
                            boolean check = pd.updateProduct(product);
                            if (check) {
                                String name = (String) model.getValueAt(selectedRow, 1);
                                boolean check1 = orderDao.cancelOrder(name);
                                if (check1) {
                                    JOptionPane.showMessageDialog(frame, "Mark Done successfully");
                                    updateTable(ordersTable, orderDao);
                           
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Something went wrong!");
                                    updateTable(ordersTable, orderDao);
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "Something went wrong!");
                                updateTable(ordersTable, orderDao);
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        markDoneButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        markDoneButton.setBounds(305, 281, 238, 36);
        markDoneButton.setBackground(new Color(102, 187, 106)); // Set button background color
        markDoneButton.setForeground(Color.BLACK); // Set button text color
        frame.getContentPane().add(markDoneButton);

        JButton refreshButton = new JButton("Refresh!");
        refreshButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		updateTable(ordersTable, orderDao);
        	}
        });
        refreshButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        refreshButton.setBounds(25, 12, 238, 36);
        refreshButton.setBackground(new Color(63, 81, 181)); // Set button background color
        refreshButton.setForeground(Color.BLACK); // Set button text color
        frame.getContentPane().add(refreshButton);

        frame.setVisible(true);
    }

    private void updateTable(JTable ordersTable, OrderDAO orderDao) {
        List<Order> orderList = orderDao.getAllOrders();
        try {
            UserDAO userDao = new UserDAO(JDBC.ConnectJDBC.getConnection());

            DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
            model.setRowCount(0);

            for (Order o : orderList) {
                User user = userDao.getUserByEmail(o.getUid());
                int quantity = o.getQuantity();
                String quantityString = Integer.toString(quantity);
                String[] row = { o.getDate(), o.getOrderName(), quantityString, user.getEmail(), user.getAddress() };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decorateTable() {
        // Set table cell alignment to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        ordersTable.setDefaultRenderer(Object.class, centerRenderer);

        // Set table header background color
        ordersTable.getTableHeader().setBackground(new Color(0, 121, 107));
        ordersTable.getTableHeader().setForeground(Color.BLACK);

        // Set table grid color
        ordersTable.setGridColor(new Color(224, 224, 224));

        // Set table row height
        ordersTable.setRowHeight(30);
    }


    
}
