package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import DAO.OrderDAO;
import Java.Cart;
import Java.Order;
import Java.User;

public class CheckOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try(PrintWriter out = response.getWriter()) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LocalDate currentDate = LocalDate.now();
            Date date = Date.valueOf(currentDate);
			// lay cart_list
            ArrayList<Cart> cart_list = (ArrayList<Cart>) request.getSession().getAttribute("cart-list");
            User auth = (User) request.getSession().getAttribute("auth");
            if(auth != null && cart_list!=null) {
            	
            	for(Cart c : cart_list) {
            		Order order = new Order();
            		order.setOrderName(c.getName());
            		order.setUid(auth.getEmail());
            		order.setQuantity(c.getQuantity());
            		order.setDate(sdf.format(date));
            		
            		OrderDAO orderDao = new OrderDAO(JDBC.ConnectJDBC.getConnection());
            		boolean result = orderDao.insertOrder(order);
            		if(!result) break;
            		
            	}
            	cart_list.clear();
            	response.sendRedirect("orders.jsp");
            	
            }else {
            	if(auth==null) response.sendRedirect("Login.jsp");
            	response.sendRedirect("cart.jsp");
            }
		} catch (Exception e) {
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
