package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.catalina.connector.Response;

import DAO.OrderDAO;
import JDBC.ConnectJDBC;
import Java.Cart;
import Java.Order;
import Java.User;
import java.sql.*;
//import java.util.*;

/**
 * Servlet implementation class OrderNowServlet
 */
public class OrderNowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try(PrintWriter out = response.getWriter();){
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			
			User auth = (User) request.getSession().getAttribute("auth");
			if(auth != null ) {

				String ProductName = request.getParameter("Name");
				int ProductQuantity = Integer.parseInt(request.getParameter("quantity"));
				if(ProductQuantity <= 0 ) {
					ProductQuantity = 1;
				}
				Order orderModel = new Order();
				
				orderModel.setOrderName(ProductName);
				orderModel.setUid(auth.getEmail());
				orderModel.setQuantity(ProductQuantity);
				
				  // Get the current date
                LocalDate currentDate = LocalDate.now();
                // Convert LocalDate to java.sql.Date
                Date date = Date.valueOf(currentDate);
				
				orderModel.setDate(sdf.format(date));
				
				
					OrderDAO orderDao = new OrderDAO(ConnectJDBC.getConnection());
					boolean result  = orderDao.insertOrder(orderModel);
					
					
					if(result) {
						ArrayList<Cart> cart_list = (ArrayList<Cart>) request.getSession().getAttribute("cart-list");
						if(cart_list != null) {
							for(Cart c : cart_list) {
								if(c.getName().equals(ProductName)) {
									cart_list.remove(cart_list.indexOf(c));
									break;
								}
							}
						}
						response.sendRedirect("orders.jsp");
					}else {
						
					}
			}else {
					response.sendRedirect("Login.jsp");
				
			}
		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}

}
