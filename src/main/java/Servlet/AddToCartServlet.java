package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.ProductDAO;
import DAO.ProductDAO.*;
import Java.*;


/**
 * Servlet implementation class AddToCartServlet
 */
public class AddToCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		
		
		

		try (PrintWriter out = response.getWriter()) {
			ArrayList<Cart> cartList = new ArrayList<>();
			ProductDAO pd = new ProductDAO(JDBC.ConnectJDBC.getConnection());
			List<Product> product_list = new ArrayList<Product>();
			product_list = pd.getAllProducts();

			String name = request.getParameter("Name");

			Cart cm = new Cart();
			cm.setName(name);
			cm.setQuantity(1);
			HttpSession session = request.getSession();
			int scrollPos = request.getParameter("scrollPos") != null ? Integer.parseInt(request.getParameter("scrollPos")) : 0;
			session.setAttribute("scrollPos", scrollPos);
			ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");

			if (cart_list == null) {
			    for (Product product : product_list) {
			        if (product.getName().equals(name) && product.getStock() > 0) {
			            cartList.add(cm);
			            session.setAttribute("cart-list", cartList);
			            response.sendRedirect("Index.jsp#product-section");




			            return;
			        }
			    }
			} else {
			    cartList = cart_list;
			    boolean exist = false;
			    cartList.contains(cm);
			    for (Cart c : cart_list) {
			        if (c.getName().equals(name)) {
			            exist = true;
			            out.println("<h3 style='color:crimson; text-align:center'>Item exists in the Cart.<a href='cart.jsp'>Go to Cart page</a></h3>");
			        }
			    }
			    for (Product product : product_list) {
			        if (product.getName().equals(name) && !exist) {
			            if (product.getStock() > 0) {
			                cartList.add(cm);
			                response.sendRedirect("Index.jsp#product-section");

			            } else {
			                out.println("<script type=\"text/javascript\">");
			                out.println("alert('Item is out of stock.');");
			                out.println("window.location.href = 'Index.jsp';");
			                out.println("</script>");
			            }
			            break;
			        }
			    }
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
