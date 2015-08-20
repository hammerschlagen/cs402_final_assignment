var users = [];
//var user = document.getElementById('username').value;
var userId;// = document.getElementById('userId').value;

$( document ).ready(function() {
	getUsers();
	userId = document.getElementById('userId').value;
});

getUsers = function() {
    $.ajax({
        url: '/bleeter/bleets/users/',// + userId,
        type: 'get',
        dataType: 'json',
        success: function(response) {
      	  //console.log(response);
            users = response;
            toHtml(users);
            console.log(JSON.stringify(users));
        },
        error: function(response) {
            alert(JSON.stringify(response));
        }
    });
}

createUser = function(username, pass, first, last) {
	if($("#create-user-form")[0].checkValidity()) {
	    $.ajax({
	        url: '/bleeter/bleets/users/',// + userId,
	        type: 'post',
	        dataType: 'json',
	        data: { username : username, password : pass, firstname: first, lastname: last},
	        success: function(response) {
	            getUsers();
	        },
	        error: function(response) {
	            alert(JSON.stringify(response));
	        }
	    });
	}
}

changeRole = function(uid, role){
    $.ajax({
        url: '/bleeter/bleets/users/' + uid + '/' + role,
        type: 'put',
        dataType: 'json',
        //data: { uid : uid, role : role},
        success: function(response) {
            //getUsers();
        	toHtml(response);
        },
        error: function(response) {
            alert(JSON.stringify(response));
        }
    });
}

toHtml = function(usrs) {
	users = usrs;
    var userNode = document.getElementById("users");
    // remove any existing locations
    while (userNode.firstChild) {
    	userNode.removeChild(userNode.firstChild);
    }
    //create header row
    var header = document.createElement('thead');
    var usernameCol = document.createElement('th');
    usernameCol.innerHTML = 'Username';    
    var firstNameCol = document.createElement('th');
    firstNameCol.innerHTML = 'First Name';
    var lastNameCol = document.createElement('th');
    lastNameCol.innerHTML = 'Last Name';
    var roleCol = document.createElement('th');
    roleCol.innerHTML = 'Role';
    header.appendChild(usernameCol);
    header.appendChild(firstNameCol);
    header.appendChild(lastNameCol);
    header.appendChild(roleCol);
    userNode.appendChild(header);
    // add users
    users.forEach(function(val, i) {
        var user = document.createElement('tr');
        user.className = "user";
        
//        var form = document.createElement('form');
//        form.className = "form-inline";
        
        var username = document.createElement('td');
        username.className = "username";
        username.innerHTML = val.username ;
        user.appendChild(username);
        
        var firstname = document.createElement('td');
        firstname.className = "firstname";
        firstname.innerHTML = val.profile['firstName'];
        user.appendChild(firstname);
        
        var lastname = document.createElement('td');
        lastname.className = "lastname";
        lastname.innerHTML = val.profile['lastName'];
        user.appendChild(lastname);

        var isAdmin = document.createElement('label');
        isAdmin.className = "radio-inline";
        isAdmin.value = "Admin";
        	var isAdminCheck = document.createElement('input');
        	isAdminCheck.name = val.id;
        	isAdminCheck.type = 'radio';
        	isAdminCheck.checked = val.roles[0] == "ROLE_ADMIN" ? "checked" : "";
        	//isAdminCheck.disabled = userId == val.id ? true : false;
        	isAdminCheck.value = "ROLE_ADMIN";
        	
        	var adminLabel = document.createElement('span');
        	adminLabel.innerHTML = "Admin";
        	
        isAdmin.appendChild(isAdminCheck);
        isAdmin.appendChild(adminLabel);
        
        var isUser = document.createElement('label');
        isUser.className = "radio-inline";
        
        	var isUserCheck = document.createElement('input');
        	isUserCheck.name = val.id;
        	isUserCheck.type = 'radio';
        	isUserCheck.checked = val.roles[0] == "ROLE_USER" ? "checked" : "";
        	//isUserCheck.disabled = userId == val.id ? disabled : "";
        	isUserCheck.value = "ROLE_USER";
        	
        	var userLabel = document.createElement('span');
        	userLabel.innerHTML = "User";

        isUser.appendChild(isUserCheck);
        isUser.appendChild(userLabel);

        var role = document.createElement('td');
        role.appendChild(isAdmin);
        role.appendChild(isUser);
        
        user.appendChild(role);
        
        userNode.appendChild(user);
       
    });
    
    $("input[type='radio']").on('click', function(e){
    	changeRole(this.name, this.value);
	});
}