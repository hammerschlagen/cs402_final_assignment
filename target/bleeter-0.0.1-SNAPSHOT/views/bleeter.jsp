<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bleeter</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
   	<link rel="stylesheet" href="css/main.css" type="text/css" />
   	<script src="https://code.jquery.com/jquery-1.9.1.js"></script>
   	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
   	<script src="js/bleeter.js"></script>
</head>
<body>
	<div class="container" style="margin-top: 25px;">
		<div class="row" style="margin-bottom: 25px;">	
			<div class="col-md-6">	
				<h1 style="font-size: 96px; font-family: Impact, Charcoal, sans-serif;">Bleeter</h1>
			</div>
			<div class="col-md-6">
				<div class="avatar pull-right">					
					<img src="/bleeter/bleets/${user.id}/avatar" alt="avatar goes here" id="userAvatar" height="140" width="140">
					<br />
					<h4 style="float: right;">${user.username}</h4>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<button type="button" class="btn btn-primary" onclick="sortBleets('owner')" >Sort By User</button>
				<button type="button" class="btn btn-primary" onclick="sortBleets('timestamp')" >Sort By Time</button>
			</div>
			<div class="col-md-6">
				<div class="pull-right">
					<c:if test="${user.roles[0] == 'ROLE_ADMIN'}">
						<span><a class="btn btn-primary" href="<c:url value="/bleets/users/manage"/>">Manage Users</a></span>
					</c:if>
					<span><button type="button" class="btn btn-primary" data-toggle="modal" href='#profileModal'>Edit Profile</button></span>
					<span><button type="button" class="btn btn-primary" onclick="getBleetsByUser()">My Bleets</button></span>
					<span><a class="btn btn-danger" href="<c:url value="/logout"/>">Logout</a></span>
				</div>
			</div>
		</div>
		<br />
		<br/>		
		<input type="hidden" id="sortOrder" value='asc'/>
		<div class="row">
			<div class="createBleet col-md-8">
				<form class="form-inline" onsubmit="return false;">
	  				<div class="checkbox">
					    <label><input type="checkbox" id="isPriv"> Private Bleet</label>
					</div>
					<br />
	  				<div class="form-group">
	  					<input type="text" class="form-control" id="bleetBox" placeholder="What's on your mind?" minlength="1" maxlength="140" required/>
	  				</div>
	  				<div class="form-group"><input type="hidden" id="userId" value='${user.id}'/></div>
	  				<input type="submit" onclick="createBleet(document.getElementById('bleetBox').value, 
	  									document.getElementById('isPriv').checked)" class="btn btn-primary" value="Bleet" />
	 			</form>
			</div>
			<div class="col-md-4" style="position: relative; top: 22px;">
				<div class="search pull-right">
					<form class="form-inline" id="search-bleets-form" onsubmit="return false;">
		  				<div class="form-group">
		  					<input type="text" class="form-control" id="searchBox" placeholder="Search by owner or time" required>
		  				</div>
		  				<input type="submit" onClick="searchBleets(document.getElementById('searchBox').value)" class="btn btn-default" value="Search" />
		  				<button type="button" class="btn btn-danger" onclick="searchBleets('')" title="Clear all filters/search results">X</button>
					</form>
				</div>
			</div>
		</div>
		
		<!-- Bleets go here -->
		<div id="bleets"></div>
		
		<!-- EDIT BLEET MODAL -->
		<div class="modal fade" id="bleetModal" tabindex="-1" role="dialog" aria-labelledby="bleetModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="bleetModalLabel">Edit Bleet</h4>
		      </div>
		      <div class="modal-body">
		        <form>
		          <div class="form-group">
		            <label for="edit-bleet-text" class="control-label">Bleet:</label>
		            <input type="text" class="form-control" id="edit-bleet-text"/>
		          </div>
		        </form>
		      </div>
		      <div class="mHidden"><input type="hidden" id="editBleetId"/></div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-primary" onclick="editBleet(document.getElementById('edit-bleet-text').value, document.getElementById('editBleetId').value)">Save Bleet</button>
		      </div>
		    </div>
		  </div>
		</div>
		<!-- EDIT PROFILE MODAL -->
		<div class="modal fade" id="profileModal" tabindex="-1" role="dialog" aria-labelledby="profModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="profModalLabel">Edit Profile</h4>
		      </div>
		      <div class="modal-body">
		        <form onsubmit="return false;">
		          <div class="form-group">
		            <label for="edit-username" class="control-label">Username:</label>
		            <input type="text" class="form-control" id="edit-username" disabled/>
		          </div>
		          <div class="form-group">
		            <label for="edit-firstname" class="control-label">First Name:</label>
		            <input type="text" class="form-control" id="edit-firstname"/>
		          </div>
		          	<div class="form-group">
		            <label for="edit-lastname" class="control-label">Last Name:</label>
		            <input type="text" class="form-control" id="edit-lastname"/>
		          </div>
		          <div class="form-group">
		            <label for="edit-email" class="control-label">Email:</label>
		            <input type="email" class="form-control" id="edit-email"/>
		          </div>
		          <div class="form-group">
		            <label for="edit-avatar" class="control-label">Avatar:</label>
		            <input type="file" class="form-control" id="edit-avatar" accept="image/*" required/>
		          </div>
   		          <div class="form-group">
		            <label for="edit-favs" class="control-label">Favorites:</label>
					<input type="hidden" name="count" value="1" />
			        <div class="control-group" id="fields">
			            <div class="controls" id="profs"> 
			                <form class="input-append">
								<input type="text" id="favorite-textbox" style="width: 93%" class="form-control" />								
								<button type="button" id="addFavoriteButton" class="btn btn-info pull-right" style="position: relative; top: -34px;" onclick="addFavorite()">
									+
								</button>
								<select size="5" id="favorites" style="width: 93%; margin-top: 5px;">
								</select>
								<button type="button" class="btn btn-danger" style="position: relative; top: -89px; float: right;" onclick="removeFavorite()">
									x
								</button>
			                    <!-- <div id="field">
				                    <input class="input form-control" id="field1" name="prof1" type="text" placeholder="Type something" data-items="8"/>
				                    <button id="b1" class="btn add-more" type="button">+</button>
				                    Add Another Favorite
			                    </div>-->
			                </form>
			            <br>
			            </div>
			        </div>
		          </div>		          
		        </form>
		      </div>
		      <div class="mHidden"><input type="hidden" id="REPLACE"/></div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary" onclick="editProfile()">Save Profile</button>
		      </div>
		    </div>
		  </div>
		</div>
		
	</div>

</body>
</html>