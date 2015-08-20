<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bleeter | User Management</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
   	<link rel="stylesheet" href="/bleeter/css/main.css" type="text/css" />
   	<script src="https://code.jquery.com/jquery-2.1.3.js"> </script>
   	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
   	<script src="/bleeter/js/manage.js"></script>
</head>
<body>
	<div class="container" style="margin-top: 25px;">
		<div class="row">
			<div class="col-md-12">
				<div class="pull-right">
					<span><a class="btn btn-primary" href="<c:url value="/"/>">Home</a></span>
					<span><a class="btn btn-danger" href="<c:url value="/logout"/>">Logout</a></span>
				</div>
			</div>
		</div>
		<h3>Create A New User</h3>
		<form class="form-inline create-user" id="create-user-form">
		  <div class="form-group">
		    <label for="username2">Username</label>
		    <br />
		    <input type="text" class="form-control" id="username2" placeholder="Username" required>
		  </div>
		  <div class="form-group">
		    <label for="password2">Password</label>
		    <br />
		    <input type="password" class="form-control" id="password2" placeholder="Password" required>
		  </div>
		  <div class="form-group">
			    <label for="firstName">First Name</label>
			    <br />
			    <input type="text" class="form-control" id="firstName" placeholder="First Name" required>
		  </div>
		  <div class="form-group">
		    <label for="lastName">Last Name</label>
		    <br />
		    <input type="text" class="form-control" id="lastName" placeholder="Last Name" required>
		  </div>
		  <div class="form-group"><input type="hidden" id="userId" value='${user.id}'/></div>
		  <div style="float: right;">
			  <input type="submit" class="btn btn-default" value="Create User" onClick="createUser(document.getElementById('username2').value,
			  																    document.getElementById('password2').value,
			  																  	document.getElementById('firstName').value,
			  																	document.getElementById('lastName').value)"></input>
		</div>
		</form>
		<h3 class="existing-users-label">Existing Users</h3>
		<table id="users"></table>
	</div>
</body>
</html>