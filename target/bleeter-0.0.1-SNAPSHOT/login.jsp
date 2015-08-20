<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bleeter|Login</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
	<link rel="stylesheet" href="css/main.css" type="text/css" />
   	<script src="https://code.jquery.com/jquery-2.1.3.js"></script>
   	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
   	<script>
   		//TODO: FIX THISvar loginError = location.search.split('error=')[1];
	</script>
</head>
<body>
	<div align="center" class="formBlock" id="loginBox">
		<form role="form" class="loginForm" action="login" method="POST">
			<h2>Please sign in</h2>
			<c:if test="${param.error != null}">
				<div class="invalidLogin" style="font-size: 24px; color: red">
					<span>Invalid username and/or password</span>
				</div>
			</c:if>	
			<label class="sr-only" for="username">Username</label> 
			<input class="form-control" type="text" id="username" placeholder="Username" required autofocus name="username"> 
			<label class="sr-only" for="inputPassword">Password</label> 
			<input class="form-control" type="password" id="password" placeholder="Password" required name="password">
			<input class="btn btn-primary" type="submit" value="Sign in">
		</form>
	</div>
</body>
</html>