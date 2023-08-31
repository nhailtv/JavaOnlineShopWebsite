package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import DAO.UserDAO;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            userDAO = new UserDAO(JDBC.ConnectJDBC.getConnection());
            String email = request.getParameter("register-email");
            String password = request.getParameter("register-password");
            String phoneNumber = request.getParameter("register-phone");
            String address = request.getParameter("register-address");

            String registered = userDAO.registerUser(email, password, phoneNumber, address);

            if (registered == null) {
                request.setAttribute("notice", registered);
                response.sendRedirect("Login.jsp");
            } else {
                request.setAttribute("notice", registered);
                request.getRequestDispatcher("Register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
