package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.apache.catalina.connector.Response;

import DAO.UserDAO;
import JDBC.ConnectJDBC;
import Java.User;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("Login.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try(PrintWriter out = response.getWriter()){
			String Email = request.getParameter("Login-email");
			String password = request.getParameter("Login-password");
			
			try {
				UserDAO userD = new UserDAO(ConnectJDBC.getConnection());
				User user = userD.userLogin(Email, password);
				if(user != null) {
					request.getSession().setAttribute("auth", user);
					response.sendRedirect("Index.jsp");
				}else {
					out.print("User Login failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
				
		}
		
	}

}
