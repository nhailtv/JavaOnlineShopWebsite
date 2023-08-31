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
List<Product> prd = pd.getAllProducts();

ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");

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
.input-group {
    position: relative;
    display: flex;
    flex-wrap: wrap;
    align-items: stretch;
    width: 50%;
     margin: 0 auto;
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

		<%
		// Create a map to store products by category
		Map<String, List<Product>> productsByCategory = new HashMap<>();

		// Iterate over all products and group them by category
		for (Product p : prd) {
			String category = p.getCategory();
			if (!productsByCategory.containsKey(category)) {
				productsByCategory.put(category, new ArrayList<>());
			}
			productsByCategory.get(category).add(p);
		}

		// Iterate over the products by category and display them
		for (Map.Entry<String, List<Product>> entry : productsByCategory.entrySet()) {
			String category = entry.getKey();
			List<Product> products = entry.getValue();
		%>
		<h3 class="mt-4"><%=category%></h3>
		<div class="row">
			<%
			for (Product p : products) {
			%>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="card mb-4">
					<img src="img/<%=p.getImage()%>" class="card-img-top" alt="...">
					<div class="card-body">
						<h5 class="card-title">
							Name:
							<%=p.getName()%>
						</h5>
						<h6 class="category">
							Category:
							<%=p.getCategory()%>
						</h6>
						<h6 class="category" style="color: blue;">
							<h6 class="price">
								Price: <span class="text-success rounded px-2 fs-5">$<%=p.getPrice()%></span>
							</h6>
							<%
							if (p.getStock() > 0) {
							%>
							<h6 class="text-primary">
								In-Stock:
								<%=p.getStock()%>
							</h6>
							<%
							if (auth != null) {
							%>
							<div class="mt-3 d-flex justify-content-between">
								<a href="add-to-cart?Name=<%=p.getName()%>" class="btn btn-dark">Add
									to cart!</a> <a
									href="order-now-servlet?quantity=1&Name=<%=p.getName()%>"
									class="btn btn-primary">Buy now!</a>
							</div>
							<%
							}
							%>
							<%
							} else {
							%>
							<h6 class="category" style="color: red;">Out of Stock</h6>
							<%
							}
							%>
							
						</h6>
					</div>
				</div>
			</div>
			<%
			}
			%>
		</div>
		<%
		}
		%>
	</div>



	<footer class="footer bg-dark text-white py-5">
		<div class="container">
			<div class="row">
				<div class="col">
					<h5>WEB-Maker:</h5>
					<ul class="list-unstyled">
						<li>Thai Ba Bau</li>
						<li>Nguyen Hong Nguyen Hai</li>
					</ul>
				</div>
				<div class="col">
					<h5>WEB-Language:</h5>
					<ul class="list-unstyled">
						<li>Java</li>
						<li>HTML</li>
						<li>CSS</li>
					</ul>
				</div>
				<div class="col">
					<h5>Contact:</h5>
					<ul class="list-unstyled">
						<li>Mail: hainhn.22it@vku.udn.vn</li>
						<li>Phone: 84-777-543-918</li>
					</ul>
				</div>
			</div>
		</div>
	</footer>


	<%@include file="includes/Footer.jsp"%>
</body>
</html>
