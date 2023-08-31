package DAO;


import java.sql.*;
import java.util.*;

import Java.Cart;
import Java.Product;

public class ProductDAO {
	private Connection conn ;
	private PreparedStatement prst;
	private ResultSet rs;
	
	public List<Product> getAllProducts(){
		List<Product> products = new ArrayList<Product>();
		try {
			prst = conn.prepareStatement("select * from product");
			rs=prst.executeQuery();
			while(rs.next()) {
				Product row = new Product();
				row.setName(rs.getString(1));
				row.setCategory(rs.getString(2));
				row.setPrice(rs.getDouble(3));
				row.setImage(rs.getString(4));
				row.setStock(rs.getInt(5));
				products.add(row);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}
	public ProductDAO(Connection conn) {
		this.conn = conn;
	}
	
	
	
	public List<Cart> getCartsProducts(ArrayList<Cart> cart_List){
		List<Cart> products = new ArrayList<Cart>();
		try {
			if(cart_List.size()>0) {
				for(Cart item : cart_List) {
					PreparedStatement prst = conn.prepareStatement("select * from product where Name = ?");
					prst.setString(1, item.getName());
					rs=prst.executeQuery();
					while(rs.next()) {
						Cart row = new Cart();
						row.setName(rs.getString("Name"));
						row.setCategory(rs.getString("Category"));
						row.setPrice(rs.getDouble("Price")*item.getQuantity());
						row.setImage(rs.getString("Image"));
						row.setQuantity(item.getQuantity());
						products.add(row);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return products;
	}
	
	public double GetTotalPrice(ArrayList<Cart> cart_list) {
		double sum = 0;
		if(cart_list!= null) {
			try {
				if(cart_list.size()>0) {
					for(Cart c : cart_list) {
						prst = conn.prepareStatement("select Price from product where Name = ?");
						prst.setString(1,c.getName());
						rs = prst.executeQuery();
						while(rs.next()) {
							sum +=rs.getDouble("Price")*c.getQuantity();
						}
					}
				
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		
		return sum;
	}
	public Product getSingleProduct(String p_ID) {
		 Product row = null;
		 System.out.println("productID:" + p_ID);
		 try {
			prst = conn.prepareStatement("select * from product where Name = ? ");
			prst.setString(1, p_ID);
			rs = prst.executeQuery();
			
			while(rs.next()) {
				row = new Product();
				row.setName(rs.getString(1));
				row.setCategory(rs.getString(2));
				row.setPrice(rs.getDouble(3));
				row.setImage(rs.getString(4));
				row.setStock(rs.getInt(5));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}
    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<Product>();
        try {
            prst = conn.prepareStatement("SELECT * FROM product WHERE Name LIKE ?");
            prst.setString(1, "%" + searchTerm + "%");
            rs = prst.executeQuery();
            while (rs.next()) {
                Product row = new Product();
                row.setName(rs.getString(1));
                row.setCategory(rs.getString(2));
                row.setPrice(rs.getDouble(3));
                row.setImage(rs.getString(4));
                row.setStock(rs.getInt(5));
                products.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
    public Product getProductByName(String productName) {
        Product product = null;
        try {
            prst = conn.prepareStatement("SELECT * FROM product WHERE Name = ?");
            prst.setString(1, productName);
            rs = prst.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setName(rs.getString(1));
                product.setCategory(rs.getString(2));
                product.setPrice(rs.getDouble(3));
                product.setImage(rs.getString(4));
                product.setStock(rs.getInt(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }
    public boolean updateProduct(Product product) {
        try {
            prst = conn.prepareStatement("UPDATE product SET Category=?, Price=?, Image=?, Stock=? WHERE Name=?");
            prst.setString(1, product.getCategory());
            prst.setDouble(2, product.getPrice());
            prst.setString(3, product.getImage());
            prst.setInt(4, product.getStock());
            prst.setString(5, product.getName());

            int rowsAffected = prst.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addProduct(Product product) {
        try {
            prst = conn.prepareStatement("INSERT INTO product (Name, Category, Price, Image, Stock) VALUES (?, ?, ?, ?, ?)");
            prst.setString(1, product.getName());
            prst.setString(2, product.getCategory());
            prst.setDouble(3, product.getPrice());
            prst.setString(4, product.getImage());
            prst.setInt(5, product.getStock());

            int rowsAffected = prst.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteProduct(String productName) {
        try {
            prst = conn.prepareStatement("DELETE FROM product WHERE Name=?");
            prst.setString(1, productName);

            int rowsAffected = prst.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<String>();
        try {
            prst = conn.prepareStatement("SELECT DISTINCT Category FROM product");
            rs = prst.executeQuery();
            while (rs.next()) {
                String category = rs.getString("Category");
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

}
