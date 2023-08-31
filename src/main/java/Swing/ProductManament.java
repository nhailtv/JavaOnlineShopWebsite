package Swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DAO.ProductDAO;
import Java.Product;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.sql.SQLException;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.JFileChooser;

public class ProductManament {

	JFrame frame;
	private JTable ProductsTable;
	private JButton EnterStockButton;
	private JButton updateButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ProductManament window = new ProductManament();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public ProductManament() throws ClassNotFoundException, SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	private void initialize() throws ClassNotFoundException, SQLException {
		ProductDAO pd = new ProductDAO(JDBC.ConnectJDBC.getConnection());
		frame = new JFrame();
		frame.setBounds(100, 100, 921, 478);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 59, 848, 213);
		frame.getContentPane().add(scrollPane);
		
		ProductsTable = new JTable();
		ProductsTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"Product Name", "Category", "Price", "Image", "Stock"
			}
		));
		ProductsTable.setRowHeight(30);
		ProductsTable.setGridColor(new Color(224, 224, 224));
		scrollPane.setViewportView(ProductsTable);
		updateTable(ProductsTable);
		
		JButton refreshButton = new JButton("Refresh!");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTable(ProductsTable);
			}
		});
		refreshButton.setForeground(Color.BLACK);
		refreshButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		refreshButton.setBackground(new Color(63, 81, 181));
		refreshButton.setBounds(34, 13, 238, 36);
		frame.getContentPane().add(refreshButton);
		
		EnterStockButton = new JButton("EnterStock");
		EnterStockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String notice = "";
				  JFileChooser fileChooser = new JFileChooser();
		            int result = fileChooser.showOpenDialog(frame);
		            if (result == JFileChooser.APPROVE_OPTION) {
		                File selectedFile = fileChooser.getSelectedFile();
		                List<String> lines = readLinesFromFile(selectedFile);
		                for (String line : lines) {
		                	String[] parts = line.split("\\s*:\\s*");
		                	Product product = pd.getProductByName(parts[0]);
		                	product.setStock(product.getStock()+Integer.parseInt(parts[1]));
		                	pd.updateProduct(product);
		                	notice = notice +" "+ parts[0]+" +" +parts[1]+"\n";
		                	updateTable(ProductsTable);
		                }
		                JOptionPane.showMessageDialog(refreshButton, notice);
		            }
			}
		});
		EnterStockButton.setForeground(Color.BLACK);
		EnterStockButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		EnterStockButton.setBackground(new Color(102, 187, 106));
		EnterStockButton.setBounds(45, 288, 238, 36);
		frame.getContentPane().add(EnterStockButton);
		
		updateButton = new JButton("Update Change");
		updateButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = ProductsTable.getSelectedRow();
		        if (selectedRow == -1) {
		            JOptionPane.showMessageDialog(frame, "Please select a row!");
		        } else {
		            try {
		                DefaultTableModel model = (DefaultTableModel) ProductsTable.getModel();
		                String productName = model.getValueAt(selectedRow, 0).toString();
		                String category = model.getValueAt(selectedRow, 1).toString();
		                double price = Double.parseDouble(model.getValueAt(selectedRow, 2).toString());
		                String image = model.getValueAt(selectedRow, 3).toString();
		                int stock = Integer.parseInt(model.getValueAt(selectedRow, 4).toString());

		                Product updatedProduct = new Product(productName, category, price, image, stock);
		                boolean updateResult = pd.updateProduct(updatedProduct);

		                if (updateResult) {
		                    JOptionPane.showMessageDialog(frame, "Update succeeded!");
		                    updateTable(ProductsTable);
		                } else {
		                    JOptionPane.showMessageDialog(frame, "Failed to update the product.");
		                }
		            } catch (NumberFormatException ex) {
		                JOptionPane.showMessageDialog(frame, "Invalid price or stock value.");
		            } catch (Exception ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(frame, "Something went wrong during the update process.");
		            }
		        }
		    }
		});
		updateButton.setForeground(Color.BLACK);
		updateButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		updateButton.setBackground(new Color(41, 182, 246));
		updateButton.setBounds(690, 282, 169, 36);
		frame.getContentPane().add(updateButton);
		
		JButton NewProductButton = new JButton("New Product");
		NewProductButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewProduct npd = new NewProduct();
				npd.frame.setVisible(true);
			}
		});
		NewProductButton.setForeground(Color.BLACK);
		NewProductButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		NewProductButton.setBackground(new Color(211, 208, 79));
		NewProductButton.setBounds(45, 334, 238, 36);
		frame.getContentPane().add(NewProductButton);
		
		JButton deleteButton = new JButton("Delete Product");
		deleteButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = ProductsTable.getSelectedRow();
		        if (selectedRow == -1) {
		            JOptionPane.showMessageDialog(frame, "Please select a row!");
		        } else {
		            try {
		                DefaultTableModel model = (DefaultTableModel) ProductsTable.getModel();
		                String productName = model.getValueAt(selectedRow, 0).toString();
		                String imageFileName = model.getValueAt(selectedRow, 3).toString(); // Assuming image file name is in column index 3
		                ProductDAO pd = new ProductDAO(JDBC.ConnectJDBC.getConnection());
		                boolean check = pd.deleteProduct(productName);
		                if (check) {
		                    // Delete the image file from the directory
		                    deleteImageFile(imageFileName);
		                    
		                    JOptionPane.showMessageDialog(frame, "Delete succeed");
		                    updateTable(ProductsTable);
		                } else {
		                    JOptionPane.showMessageDialog(frame, "Something went wrong");
		                    updateTable(ProductsTable);
		                }
		            } catch (Exception e2) {
		                e2.printStackTrace();
		            }
		        }
		    }
		});
		deleteButton.setForeground(Color.BLACK);
		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		deleteButton.setBackground(new Color(244, 67, 54));
		deleteButton.setBounds(400, 282, 169, 36);
		frame.getContentPane().add(deleteButton);

		
	}
	private void updateTable(JTable Table) {
		try {
			ProductDAO pd = new ProductDAO(JDBC.ConnectJDBC.getConnection());
			DefaultTableModel model = (DefaultTableModel) Table.getModel();
			List<Product> product_list = pd.getAllProducts();
			 model.setRowCount(0);
			 for(Product p: product_list) {
				 int stock = p.getStock();
				 double price = p .getPrice();
				 String StringStock = Integer.toString(stock);
				 String StringPrice = Double.toString(price);
				 String[] row = {p.getName(),p.getCategory(),StringPrice,p.getImage(),StringStock};
				 model.addRow(row);
			 }
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
		
	}
	
	private List<String> readLinesFromFile(File file) {
	    List<String> lines = new ArrayList<>();
	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            lines.add(line);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return lines;
	}
	
	 private void decorateTable() {
	        // Set table cell alignment to center
	        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
	        ProductsTable.setDefaultRenderer(Object.class, centerRenderer);

	        // Set table header background color
	        ProductsTable.getTableHeader().setBackground(new Color(0, 121, 107));
	        ProductsTable.getTableHeader().setForeground(Color.BLACK);

	        // Set table grid color
	        ProductsTable.setGridColor(new Color(224, 224, 224));

	        // Set table row height
	        ProductsTable.setRowHeight(30);
	    }
	 private void deleteImageFile(String imageFileName) {
		    String directoryPath = "C:/Users/Admin/eclipse-workspace/Finals/src/main/webapp/img/"; // Specify the directory path where the images are stored
		    File imageFile = new File(directoryPath, imageFileName);
		    if (imageFile.exists()) {
		        boolean deleted = imageFile.delete();
		        if (deleted) {
		            System.out.println("Image file deleted successfully.");
		        } else {
		            System.out.println("Failed to delete the image file.");
		        }
		    } else {
		        System.out.println("Image file does not exist in the specified directory.");
		    }
		}
}
