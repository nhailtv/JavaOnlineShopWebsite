package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        // Redirect to search.jsp
	        request.getRequestDispatcher("search.jsp").forward(request, response);
	    }

	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        // Handle the search form submission
	        String searchTerm = request.getParameter("search");

	        // Validate the search term if needed

	        // Redirect to search.jsp with the search term as a parameter
	        response.sendRedirect("search.jsp?search=" + searchTerm);
	    }

}
