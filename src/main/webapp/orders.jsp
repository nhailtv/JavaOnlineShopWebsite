<%@page import="java.util.List"%>
<%@page import="DAO.OrderDAO"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="JDBC.ConnectJDBC"%>
<%@page import="Java.*"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
DecimalFormat dcf = new DecimalFormat("#.##");
request.setAttribute("dcf", dcf);
User auth = (User) request.getSession().getAttribute("auth");
List<Order> orders = null;
if (auth != null) {
	request.setAttribute("auth", auth);
	orders = new OrderDAO(ConnectJDBC.getConnection()).userOrder(auth.getEmail());

} else {
	//response.sendRedirect("Login.jsp");
}
ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");

if (cart_list != null) {
	request.setAttribute("cart_list", cart_list);
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@include file="includes/Head.jsp"%>
<title>Orders</title>

</head>
<body>
	<!-- NavBar -->
	<nav class="navbar navbar-expand-lg bg-body-tertiary">
		<div class="container">
			<a class="navbar-brand" href="Index.jsp"> <img src="img/Logo.png"
				alt="GoodGear Logo" class="nav-logo"> <span class="brand-text">GoodGear</span>
			</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
				aria-controls="navbarSupportedContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse justify-content-between"
				id="navbarSupportedContent">
				<ul class="navbar-nav mx-auto">
					<li class="nav-item"><a class="nav-link" aria-current="page"
						href="Index.jsp"><i class="fas fa-home"></i> Home</a></li>
					<%
					if (auth != null) {
						if (cart_list != null && cart_list.size() > 0) {
					%>
					<li class="nav-item"><a class="nav-link" href="cart.jsp">
							<i class="fas fa-shopping-cart"></i> Cart <span
							class="badge bg-danger px-1">${cart_list.size()}</span>
					</a></li>
					<%
					} else {
					%>
					<li class="nav-item"><a class="nav-link" href="cart.jsp"><i
							class="fas fa-shopping-cart"></i> Cart</a></li>
					<%
					}
					%>
					<li class="nav-item"><a class="nav-link active"
						href="orders.jsp"><i class="fas fa-clipboard-list"></i> Order</a></li>
					<%
					} else {
					%>
					<li class="nav-item"><a class="nav-link" href="Login.jsp"><i
							class="fas fa-sign-in-alt"></i> Login</a></li>
					<%
					}
					%>
				</ul>
				<ul class="navbar-nav">
					<%
					if (auth != null) {
					%>
					<li class="nav-item"><a class="nav-link" href="log-out"><i
							class="fas fa-sign-out-alt"></i> Logout</a></li>
					<%
					}
					%>
				</ul>
			</div>
		</div>
	</nav>


<div class="container">
	<div class="card-header my-3">All Orders</div>
	<div class="row">
		<%
		if (orders != null) {
			for (Order o : orders) {
		%>
		<div class="col-md-4">
			<div class="card mb-3">
				<div class="card-body">
					<h5 class="card-title" style="color : #1D5B79"><%=o.getOrderName()%></h5>
					<p class="card-text">Date: <%=o.getDate()%></p>
					<p class="card-text">Category: <%=o.getCategory()%></p>
					<p class="card-text">Quantity: <%=o.getQuantity()%></p>
					<p class="card-text">Price: $<%=dcf.format(o.getPrice())%></p>
					<a class="btn btn-sm btn-danger" href="cancel-order-servlet?Name=<%=o.getOrderName()%>">Cancel</a>
				</div>
			</div>
		</div>
		<%
		}
		} else {
		System.out.print("null orders");
		}
		%>
	</div>
</div>



	<%@include file="includes/Footer.jsp"%>
</body>
</html>