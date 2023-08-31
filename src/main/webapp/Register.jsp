 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="Java.*"%>
<%@page import="java.util.*"%>
<%
User auth = (User) request.getSession().getAttribute("auth");
if (auth != null) {
	response.sendRedirect("Index.jsp");
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
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="css/stylesRegister.css">
</head>
<body>

    <!-- NavBar -->
	<nav class="navbar navbar-expand-lg bg-body-tertiary">
		<div class="container">
			<a class="navbar-brand" href="Index.jsp">
				<img src="img/Logo.png" alt="GoodGear Logo" class="nav-logo">
				<span class="brand-text">GoodGear</span>	
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
							<i class="fas fa-shopping-cart"></i> Cart
							<span class="badge bg-danger px-1">${cart_list.size()}</span>
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
      
    <% 
    String notice = (String) request.getAttribute("notice");
    if (notice != null) {
    %>
    <p style="color: red ; display: flex; justify-content: center; "><%= notice %></p>
    <% 
    }
    %>
    <section class="vh-100" style="padding-top: 30px">
        <div class="container-fluid h-custom">
            <div class="row d-flex justify-content-center align-items-center h-100">
                
                <div class="col-md-8 col-lg-6 col-xl-4 offset-xl-1">
                    <form action="register-servlet" method="post">

                        <div class="d-flex flex-row align-items-center justify-content-center justify-content-lg-start">
                            <p class="lead fw-normal mb-0 me-3">Register with</p>
                            <button type="button" class="btn btn-primary btn-floating mx-1">
                                <i class="fab fa-facebook-f"></i>
                            </button>
                            <button type="button" class="btn btn-primary btn-floating mx-1">
                                <i class="fab fa-twitter"></i>
                            </button>
                            <button type="button" class="btn btn-primary btn-floating mx-1">
                                <i class="fab fa-linkedin-in"></i>
                            </button>
                        </div>

                        <div class="divider d-flex align-items-center my-4">
                            <p class="text-center fw-bold mx-3 mb-0">Or</p>
                        </div>

                        <!-- Email input -->
                        <div class="form-outline mb-4">
                            <label for="registerEmail" class="form-label">Email address</label>
                            <input type="email" id="registerEmail" class="form-control form-control-lg"
                                   placeholder="Enter a valid email address" name="register-email" required="required">
                        </div>

                        <!-- Password input -->
                        <div class="form-outline mb-4">
                            <label for="registerPassword" class="form-label">Password</label>
                            <input type="password" id="registerPassword" class="form-control form-control-lg"
                                   placeholder="Enter password" name="register-password" required="required">
                        </div>

                        <!-- Phone number input -->
                        <div class="form-outline mb-4">
                            <label for="registerPhone" class="form-label">Phone number</label>
                            <input type="text" id="registerPhone" class="form-control form-control-lg"
                                   placeholder="Enter your phone number" name="register-phone" required="required">
                        </div>

                        <!-- Address input -->
                        <div class="form-outline mb-4">
                            <label for="registerAddress" class="form-label">Address</label>
                            <input type="text" id="registerAddress" class="form-control form-control-lg"
                                   placeholder="Enter your address" name="register-address" required="required">
                        </div>

                        <div class="text-center text-lg-start mt-4 pt-2">
                            <button type="submit" class="btn btn-primary btn-lg"
                                    style="padding-left: 2.5rem; padding-right: 2.5rem;">Register</button>
                            <p class="small fw-bold mt-2 pt-1 mb-0">
                                Already have an account? <a href="Login.jsp" class="link-danger">Login</a>
                            </p>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </section>

    <%@include file="includes/Footer.jsp"%>
</body>
</html>
 