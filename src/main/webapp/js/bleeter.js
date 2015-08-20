var bleets = [];
var userId;
var user;
//var sortOrder;

$( document ).ready(function() {
	userId = document.getElementById('userId').value;
	getBleets(userId);
	getUser();
	//sortOrder = 'asc';
	
	$('#bleetModal').on('show.bs.modal', function (event) {
		  var button = $(event.relatedTarget);// Button that triggered the modal
		  var text = button.data('text'); // Extract info from data-* attributes
		  var bid = button.data('id');
		  // Update the modal's content. 
		  $('#edit-bleet-text').val(text);
		  $('#editBleetId').val(bid);
	});
	
	$('#profileModal').on('show.bs.modal', function (event) {
		  // Update the modal's content. 
		  $('#edit-username').val(user.username);
		  $('#edit-firstname').val(user.profile['firstName']);
		  $('#edit-lastname').val(user.profile['lastName']);
		  $('#edit-email').val(user.profile['email']);
		  
		  var favSelect = document.getElementById('favorites');
		  var favs = user.profile['favorites'];

		  for(var i = 0; i < favs.length; i += 1)
		  {
		     var opt = document.createElement("option");
		     opt.value = favs[i];
		     opt.innerHTML = favs[i];

		     favSelect.appendChild(opt);
		  }
	});
	
});

//only used to fill profile data
getUser = function(){
    $.ajax({
        url: '/bleeter/bleets/users/' + userId,
        type: 'get',
        dataType: 'json',
        success: function(response) {
            user = response;
        },
        error: function(response) {
            alert(JSON.stringify(response));
        }
    });
}

function removeFavorite() {
    var index = $("#favorites option:selected").index();
    var size = $("#favorites option").size();
    $("#favorites option:selected").remove();
    if (index == size - 1 && size > 1)
        index--;
    if (size != 1) {
        $('#favorites option')[index].selected = true;
    }
    $('#favorites').focus();
}

function addFavorite() {
    var choices = document.getElementById('favorites');
    var newChoice = document.getElementById('favorite-textbox').value;
    if (newChoice.trim() != "") {
        choices.options[choices.options.length] = new Option(newChoice, newChoice);
        document.getElementById('favorite-textbox').value = "";
        document.getElementById('favorite-textbox').focus();
    }
}

searchBleets = function(sText) {
	if (sText.length == 0){$("#searchBox").val("");};
	$.ajax({
		url: '/bleeter/bleets/' + userId + '?search=' + sText,
		type: 'get',
		datatype: 'json',
		success: function(response){
			bleets = response;
			toHtml(bleets);
			
		},
		error: function(response){
			alert(JSON.stringify(response));
		}
	});
}

getBleets = function() {
    $.ajax({
        url: '/bleeter/bleets/' + userId,
        type: 'get',
        dataType: 'json',
        success: function(response) {
            bleets = response;
            toHtml(bleets);
        },
        error: function(response) {
            alert(JSON.stringify(response));
        }
    });
}

getBleetsByUser = function() {
    $.ajax({
        url: '/bleeter/bleets/' + userId + '?filter=owned',
        type: 'get',
        dataType: 'json',
        success: function(response) {
            bleets = response;
            toHtml(bleets);
        },
        error: function(response) {
            alert(JSON.stringify(response));
        }
    });
}

createBleet = function(bleet, isPriv){
	if (bleet.length == 0){return false};
	$.ajax({
		url: '/bleeter/bleets',
		type: 'post',
		data: { uid : userId, bleet : bleet, isPriv : isPriv},
		success: function(response){
			getBleets(userId);
			$("#bleetBox").val("");
		},
		error: function(response){
			alert(JSON.stringify(response));
		}
	});
	return false;
}

editBleet = function(text, bid){
	$.ajax({
		url: '/bleeter/bleets/' + userId + '/' + bid + '/' + text,
		type: 'put',
		datatype: 'json',
		success: function(response){
			getBleets(userId);
			$('#bleetModal').modal('hide');
		},
		error: function(response){
			alert(JSON.stringify(response));
		}
	});
}

deleteBleet = function(bleetId){
	$.ajax({
		url: '/bleeter/bleets/' + userId + '/' + bleetId,
		type: 'delete',
		success: function(response){
			getBleets(userId);
		},
		error: function(response){
			alert(JSON.stringify(response));
		}
	});
}

blockBleet = function(bleetId, block){
	$.ajax({
		url: '/bleeter/bleets/' + bleetId + '/' + block,
		type: 'put',
		success: function(response){
            bleets = response;
            toHtml(bleets);
		},
		error: function(response){
			alert(JSON.stringify(response));
		}
	});
}

editProfile = function(){
	
	var username = document.getElementById('edit-username').value;
	var first = document.getElementById('edit-firstname').value;
	var last = document.getElementById('edit-lastname').value;
	var email = document.getElementById('edit-email').value;
	
	var imageFile = document.getElementById('edit-avatar').files[0];
	
	var fav = document.getElementById('favorites');
	var favs = new Array();

	for(var i=0; i < fav.options.length; i++){
	    favs.push(fav.options[i].value);
	}
	
	var data = new FormData();
	data.append('username', username);
	data.append('firstname', first);
	data.append('lastname', last);
	data.append('email', email);
	data.append('avatar', imageFile);
	data.append('favorites', favs);
	
	$.ajax({
		url: '/bleeter/bleets/users/' + userId,
		type: 'post',
		data: data,
		cache : false,
		dataType : 'text',
		processData : false, 
		contentType : false,
		success: function(response){
			var src = $("#userAvatar").attr("src");
			$('#userAvatar').attr('src', src + '?' + new Date().getTime());//cache workaround
			$('#profileModal').modal('hide');
			location.reload();
		},
		error: function(response){
			alert("Please choose an avatar image");
			//alert(JSON.stringify(response));
		}
	});
}

sortBleets = function(sort){
	var order = $('#sortOrder').val();
	
    $.ajax({
        url: '/bleeter/bleets/' + userId + "?sort=" + sort + "&order=" + order,
        type: 'get',
        dataType: 'json',
        success: function(response) {
            bleets = response;
            toHtml(bleets);
            if (order == 'asc'){
            	$('#sortOrder').val('desc');
            } else {
            	$('#sortOrder').val('asc');            }
            
        },
        error: function(response) {
            alert(JSON.stringify(response));
        }
    });
}

toHtml = function(bleets) {
    var bleetNode = document.getElementById("bleets");
    // remove any existing locations
    while (bleetNode.firstChild) {
    	bleetNode.removeChild(bleetNode.firstChild);
    }
    // add users
    bleets.forEach(function(val, i) {
        var bleet = document.createElement('div');
        bleet.className = "bleet";
        
        var header = document.createElement('div');
        header.className = "bleet-header";
        
        var username = document.createElement('span');
        username.className = "username";
        username.innerHTML = val.owner;
        header.appendChild(username);
        
        var timestamp = document.createElement('span');
        timestamp.className = "timestamp";
        timestamp.innerHTML = new Date (val.timestamp).toLocaleString();
        header.appendChild(timestamp);
        
        var bleetControls = document.createElement('span');
        bleetControls.className = "bleet-controls";
                
        if (user.roles[0] == "ROLE_ADMIN"){
        	var blockSpan = document.createElement('label');
        	blockSpan.className = 'block-bleet';        	
        	
            var blockBox = document.createElement('input');
            blockBox.className = "checkbox " + (val.isBlocked ? "checked" : "");
            blockBox.type = "checkbox";
            blockBox.name = val.id;
            
            blockSpan.appendChild(blockBox);
            //blockBox.checked = val.isBlocked ? true : false;
            blockSpan.innerHTML += ' Block This Bleet';
            bleetControls.appendChild(blockSpan);
        }
        
        
	        var editBtn = document.createElement('button');
	        editBtn.className = "edit btn btn-xs btn-default glyphicon glyphicon-edit";
	        editBtn.setAttribute('data-toggle', 'modal');
	        editBtn.setAttribute('href', '#bleetModal');
	        editBtn.setAttribute('data-text', val.bleet);
	        editBtn.setAttribute('data-id', val.id);
	        //hide this to make checkboxes line up
	        if (val.ownerId != userId){
	        	editBtn.style.visibility = "hidden";
	        }
	        bleetControls.appendChild(editBtn);
	        
	        var deleteBtn = document.createElement('span');
	        deleteBtn.className = "delete btn btn-xs btn-danger glyphicon glyphicon-trash";
	        deleteBtn.addEventListener('click', function() { deleteBleet(val.id); } );
	        if (val.ownerId != userId){
	        	deleteBtn.style.visibility = "hidden";
	        }
	        bleetControls.appendChild(deleteBtn);
        
        
        header.appendChild(bleetControls);
        bleet.appendChild(header);
        
        var content = document.createElement('span');
        content.className = "content";
        content.innerHTML = val.bleet;
        bleet.appendChild(content);
        
        bleetNode.appendChild(bleet);
       
    });
    
    $("#bleets input[type='checkbox']").on('click', function(e){
    	blockBleet(this.name, this.checked);
	});
    
    $("input.checked").prop('checked', 'checked').removeClass("checked");
}