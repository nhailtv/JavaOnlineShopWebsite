<%@page import="java.text.DecimalFormat"%>
<%@page import="DAO.ProductDAO"%>
<%@page import="java.util.*"%>
<%@page import="JDBC.ConnectJDBC"%>
<%@page import="Java.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
DecimalFormat dcf = new DecimalFormat("#.##");
request.setAttribute("dcf", dcf);
User auth = (User) request.getSession().getAttribute("auth");
if (auth != null) {
	request.setAttribute("auth", auth);
}
ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");
List<Cart> cartProducts = null;
double total  = 0;
if (cart_list != null) {
	ProductDAO pDao = new ProductDAO(ConnectJDBC.getConnection());
	cartProducts = pDao.getCartsProducts(cart_list);
	total = pDao.GetTotalPrice(cart_list);
	request.setAttribute("cart_list", cart_list);
	request.setAttribute("total", total);
}
%>
<!DOCTYPE html>
<html>


<head>


<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@include file="includes/Head.jsp"%>

<style type="text/css">
.table tbody td {
	vartical-align: middle;
}

<
style type ="text /css ">.table tbody td {
	vertical-align: middle;
}

@media ( max-width : 576px) {
	.navbar .nav-link {
		padding: 0.5rem 0.75rem;
	}
}

@media ( max-width : 768px) {
	.form-group.w-50 {
		width: 30%;
	}
}

@media ( max-width : 992px) {
	.form-group.w-50 {
		width: 40%;
	}
}
</style>

</style>
<meta charset="ISO-8859-1">
<title>Cart page</title>
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
					<li class="nav-item"><a class="nav-link active"
						href="cart.jsp"> <i class="fas fa-shopping-cart"></i> Cart <span
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
	<div class="col-md-6" style="margin: 0 auto; padding-top: 10px;">
		<div class="card">
			<div class="card-body">
				<h5 class="card-title">
					Total Price: $<%=dcf.format(total)%></h5>
				<a href="check-out-servlet" class="btn btn-primary">Checkout</a>
			</div>
		</div>
	</div>


	<div class="container" style="margin-top:10px;">
		<div class="row">
			<%
			if (cart_list != null) {
				for (Cart c : cartProducts) {
			%>
			<div class="col-md-6 col-lg-4 mb-4">
				<div class="card">
					<div class="row no-gutters">
						<div class="col-md-8">
							<div class="card-body">
								<h5 class="card-title"><%=c.getName()%></h5>
								<h6 class="card-subtitle mb-2 text-muted"><%=c.getCategory()%></h6>
								<p class="card-text">
									$<%=dcf.format(c.getPrice())%></p>
								<form action="order-now-servlet" method="post">
									<input type="hidden" name="Name" value="<%=c.getName()%>"
										class="form-input">
									<div class="form-group">
										<div class="d-flex justify-content-between align-items-center">
											<a class="btn btn-sm btn-decre"
												href="quantity-inc-dec-servlet?action=dec&Name=<%=c.getName()%>"><i
												class="fas fa-minus-square"></i></a> <input type="text"
												name="quantity" class="form-control"
												value="<%=c.getQuantity()%>" readonly>
											<%
											ProductDAO pd = new ProductDAO(ConnectJDBC.getConnection());
											Product FD = pd.getProductByName(c.getName());
											if (c.getName().equals(FD.getName())) {
												if (c.getQuantity() < FD.getStock()) {
											%>
											<a class="btn btn-sm btn-incre"
												href="quantity-inc-dec-servlet?action=inc&Name=<%=c.getName()%>"><i
												class="fas fa-plus-square"></i></a>
											<%
											}
											}
											%>
										</div>
									</div>
									<div
										class="d-flex justify-content-between align-items-center mt-3">
										<button type="submit" class="btn btn-primary btn-sm">Buy
											now</button>
										<a class="btn btn-sm btn-danger"
											href="remove-from-cart-servlet?Name=<%=c.getName()%>">Remove</a>
									</div>
								</form>
							</div>
						</div>
						<div class="col-md-4">
							<img src="img/<%=c.getImage()%>"
								class="card-img-top img-thumbnail" alt="Product Image">
						</div>
					</div>
				</div>
			</div>
			<%
			}

			}
			%>


		</div>
	</div>



	<%@include file="includes/Footer.jsp"%>
</body>
</html>