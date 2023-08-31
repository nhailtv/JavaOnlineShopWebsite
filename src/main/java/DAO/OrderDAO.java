package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Java.Order;
import Java.Product;

public class OrderDAO {
	private Connection conn ;
	private PreparedStatement prst;
	private ResultSet rs;
	
	public OrderDAO(Connection conn)
	{
		this.conn = conn;
	}
	public boolean insertOrder(Order order)
	{
		boolean result = false;
		
		try {
			
			PreparedStatement prst = conn.prepareStatement("insert into orders (p_id,u_id,o_quantity,o_date) values(?,?,?,?)");
			prst.setString(1,order.getOrderName());
			System.out.println(order.getOrderName());
			prst.setString(2, order.getUid());
			prst.setInt(3,order.getQuantity());
			prst.setString(4, order.getDate());
			prst.executeUpdate();
			result = true;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return result;
	}
	
	public List<Order> userOrder(String UMail){
		List<Order> order_list = new ArrayList<Order>();
		try {
			prst = conn.prepareStatement("select * from orders where u_id = ? order by orders.o_id desc");
			prst.setString(1,UMail);
			rs = prst.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				ProductDAO productDao = new ProductDAO(this.conn);
				String p_ID = rs.getString("p_id");
				Product product  = productDao.getSingleProduct(p_ID);
				order.setOrderName(p_ID);
				
				order.setCategory(product.getCategory());
				order.setPrice(product.getPrice()*rs.getInt("o_quantity"));
				order.setQuantity(rs.getInt("o_quantity"));
				order.setDate(rs.getString("o_date"));
				order_list.add(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order_list;
	}
	
	public boolean cancelOrder(String Name) {
	    try {
	        prst = conn.prepareStatement("DELETE FROM orders WHERE p_id = ?");
	        prst.setString(1, Name);
	        int rowsAffected = prst.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean updateOrder(Order order) {
	    boolean result = false;

	    try {
	        PreparedStatement prst = conn.prepareStatement("UPDATE orders SET o_quantity=?, o_date=? WHERE p_id=? AND u_id=?");
	        prst.setInt(1, order.getQuantity());
	        prst.setString(2, order.getDate());
	        prst.setString(3, order.getOrderName());
	        prst.setString(4, order.getUid());
	        prst.executeUpdate();
	        result = true;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return result;
	}
	public List<Order> getAllOrders() {
	    List<Order> orderList = new ArrayList<>();

	    try {
	        PreparedStatement prst = conn.prepareStatement("SELECT * FROM orders");
	        ResultSet rs = prst.executeQuery();

	        while (rs.next()) {
	            Order order = new Order();
	            order.setOrderName(rs.getString("p_id"));
	            order.setUid(rs.getString("u_id"));
	            order.setQuantity(rs.getInt("o_quantity"));
	            order.setDate(rs.getString("o_date"));
	            orderList.add(order);
	        }

	        rs.close();
	        prst.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return orderList;
	}

	
}
