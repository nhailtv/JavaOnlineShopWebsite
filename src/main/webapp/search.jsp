<%@page import="DAO.ProductDAO"%>
<%@page import="JDBC.ConnectJDBC"%>
<%@page import="Java.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
User auth = (User) request.getSession().getAttribute("auth");
if (auth != null) {
	request.setAttribute("auth", auth);
}

ProductDAO pd = new ProductDAO(ConnectJDBC.getConnection());
List<Product> prd;
ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");

String searchTerm = request.getParameter("search");
if (searchTerm != null && !searchTerm.trim().isEmpty()) {
	prd = pd.searchProducts(searchTerm);
} else {
	prd = pd.getAllProducts();
}

if (cart_list != null) {
	request.setAttribute("cart_list", cart_list);
}
%>

<!doctype html>
<html lang="en">
<head>
<%@include file="includes/Head.jsp"%>
<title>Index</title>
<style>
.card-img-top {
	height: 300px;
	object-fit: cover;
}
</style>
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
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="Index.jsp"><i class="fas fa-home"></i>
							Home</a></li>
					<%
					if (auth != null) {
					%>
					<%
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
					<li class="nav-item"><a class="nav-link" href="orders.jsp"><i
							class="fas fa-clipboard-list"></i> Order</a></li>
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

	<!-- ... -->
<div class="container">
		<form action="search-servlet" method="get" class="my-3">
			<div class="input-group">
				<input type="text" name="search" class="form-control"
					placeholder="Search" style="width: 50%">
				<button class="btn btn-primary" type="submit">
					<i class="fas fa-search"></i>
				</button>
			</div>
		</form>
    </div>

	<div class="container">
		<div class="card-header my-3">All Products</div>
		<div class="row">
			<%
			if (!prd.isEmpty()) {
				for (Product p : prd) {
			%>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="card mb-4">
					<img src="img/<%=p.getImage()%>" class="card-img-top"
						alt="Product Image">
					<div class="card-body">
						<h5 class="card-title">
							Name:<%=p.getName()%></h5>
						<h6 class="cagetory">
							Cagetory:<%=p.getCategory()%></h6>


						<%
						if (p.getStock() > 0) {
						%>
						<h6 class="price">
							price: <span class="text-success rounded px-2 fs-5">$<%=p.getPrice()%></span>
						</h6>

						<p class="card-text text-success">
							<h6 class="text-primary">
								In-Stock:
								<%=p.getStock()%>
							</h6>
						<%
						} else {
						%>
						<p class="card-text text-danger">Out of Stock</p>
						<%
						}
						%>
						<%
						if (auth != null) {
						%>
						<div class="mt-3 d-flex justify-content-between">
							<a href="add-to-cart?Name=<%=p.getName()%>" class="btn btn-dark">Add
								to Cart</a> <a
								href="order-now-servlet?quantity=1&Name=<%=p.getName()%>"
								class="btn btn-primary">Buy Now</a>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<div class="col">
				<p>No products found.</p>
			</div>
			<%
			}
			%>
		</div>
	</div>

	<!-- ... -->

	<footer class="footer bg-dark text-white py-5">
		<!-- Footer content -->
	</footer>



	<%@include file="includes/Footer.jsp"%>
</body>
</html>
