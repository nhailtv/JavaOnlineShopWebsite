package Swing;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import DAO.ProductDAO;
import Java.Product;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class NewProduct {

    JFrame frame;
    private JTextField textFieldName;
    private JTextField textFieldCategory;
    private JTextField textFieldPrice;
    private JTextField textFieldStock;
    private JLabel lblImageName;
    String fileName;
    File SelectedFile;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    NewProduct window = new NewProduct();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public NewProduct() {
        initialize();
    }

    private void initialize() {
    	
        frame = new JFrame();
        frame.setBounds(100, 100, 404, 366);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.PLAIN, 16));
        lblName.setBounds(50, 50, 70, 20);
        frame.getContentPane().add(lblName);

        textFieldName = new JTextField();
        textFieldName.setFont(new Font("Arial", Font.PLAIN, 16));
        textFieldName.setBounds(130, 50, 200, 30);
        frame.getContentPane().add(textFieldName);
        textFieldName.setColumns(10);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setFont(new Font("Arial", Font.PLAIN, 16));
        lblCategory.setBounds(50, 90, 90, 20);
        frame.getContentPane().add(lblCategory);

        textFieldCategory = new JTextField();
        textFieldCategory.setFont(new Font("Arial", Font.PLAIN, 16));
        textFieldCategory.setBounds(130, 90, 200, 30);
        frame.getContentPane().add(textFieldCategory);
        textFieldCategory.setColumns(10);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPrice.setBounds(50, 130, 70, 20);
        frame.getContentPane().add(lblPrice);

        textFieldPrice = new JTextField();
        textFieldPrice.setFont(new Font("Arial", Font.PLAIN, 16));
        textFieldPrice.setBounds(130, 130, 200, 30);
        frame.getContentPane().add(textFieldPrice);
        textFieldPrice.setColumns(10);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setFont(new Font("Arial", Font.PLAIN, 16));
        lblStock.setBounds(50, 170, 70, 20);
        frame.getContentPane().add(lblStock);

        textFieldStock = new JTextField();
        textFieldStock.setFont(new Font("Arial", Font.PLAIN, 16));
        textFieldStock.setBounds(130, 170, 200, 30);
        frame.getContentPane().add(textFieldStock);
        textFieldStock.setColumns(10);

        JLabel lblImg = new JLabel("Import Image");
        lblImg.setFont(new Font("Arial", Font.PLAIN, 16));
        lblImg.setBounds(50, 213, 120, 20);
        frame.getContentPane().add(lblImg);

        lblImageName = new JLabel();
        lblImageName.setFont(new Font("Arial", Font.PLAIN, 16));
        lblImageName.setBounds(28, 254, 400, 20);
        frame.getContentPane().add(lblImageName);

        JButton btnNewButton = new JButton("Commit Image");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnNewButton.setBounds(168, 210, 147, 30);
        frame.getContentPane().add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("ADD");
        btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 23));
        btnNewButton_1.setBounds(91, 284, 225, 35);
        frame.getContentPane().add(btnNewButton_1);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ProductDAO pd = new ProductDAO(JDBC.ConnectJDBC.getConnection());
                    Product product = new Product();
                    String name = textFieldName.getText();
                    String category = textFieldCategory.getText();
                    String priceText = textFieldPrice.getText();
                    String stockText = textFieldStock.getText();

                    if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || fileName.isEmpty()) {
                        // Show error message if any of the fields is empty
                        JOptionPane.showMessageDialog(frame, "Please fill in all information.");
                    } else {
                        // All fields have values, proceed with creating the Product object
                        product.setName(name);
                        product.setCategory(category);
                        product.setImage(fileName);
                        product.setPrice(Double.parseDouble(priceText));
                        product.setStock(Integer.parseInt(stockText));
                        boolean check = pd.addProduct(product);
                        System.out.println(check);
                        if (check) {
                            // Copy the selected image to a specific directory
                            String destinationDirectory = "C:/Users/Admin/eclipse-workspace/Finals/src/main/webapp/img/"; // Replace with your desired directory
                            copyImageToDirectory(SelectedFile, destinationDirectory);
                            JOptionPane.showMessageDialog(frame, "Add done!");
                            frame.dispose(); 
                        } else {
                            JOptionPane.showMessageDialog(frame, "Something went wrong!");
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    SelectedFile = fileChooser.getSelectedFile();
                    fileName = SelectedFile.getName();
                    lblImageName.setText("Selected Image: " + fileName);
                }
            }
        });
    }
    private void copyImageToDirectory(File sourceFile, String destinationDirectory) throws IOException {
        String fileName = sourceFile.getName();
        Path destinationPath = Path.of(destinationDirectory, fileName);
        Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }


}
