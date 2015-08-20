package com.cs402.bleeter.bleets;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cs402.bleeter.users.BleeterUser;
import com.cs402.bleeter.users.UserService;

@Controller
public class BleetController {
	@Autowired
	BleetService bleetService;
	@Autowired
	UserService userService;
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping(value = "/bleets/{uid}", method=RequestMethod.GET)
	@PreAuthorize(value = "principal.id == #uid")
	public @ResponseBody List<Bleet> getBleets(@PathVariable String uid, 
												@RequestParam(value="filter", required=false, defaultValue= "all") String filter,
												@RequestParam(value="sort", required=false, defaultValue = "timestamp") String sort,
												@RequestParam(value="order", required=false, defaultValue = "asc") String order,
												@RequestParam(value="search", required=false) String search){
		
		BleeterUser user = userService.findById(uid);
		
		Sort s;
		if (order.equals("asc")){
			s = new Sort(Direction.ASC, sort);
		} else {
			s = new Sort(Direction.DESC, sort);
		}
		System.out.println(sort.toString());
		System.out.println(s.toString());
		if (search != null && !search.equals("")){
			if (user.getRoles().get(0).equals("ROLE_ADMIN")){
				return bleetService.search(search, s);
			}
			return bleetService.nonAdminSearch(search, s);
		}
		
		if (filter.equals("all")){
			if (user.getRoles().get(0).equals("ROLE_ADMIN")){
				return bleetService.findAll(s);
			}
			return bleetService.findAllPublicAndUnblocked(s);
		}
		
		return bleetService.findAllByOwnerId(uid, s);
	}
	
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping(value = "/bleets", method=RequestMethod.POST)
	public @ResponseBody BleeterUser addBleet(@RequestParam String uid, @RequestParam String bleet, @RequestParam Boolean isPriv){
		
		BleeterUser user = userService.findById(uid);
		
		if (user != null){
			try {
				bleetService.createBleet(user, bleet, isPriv);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return user;
	}
	
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PreAuthorize(value = "principal.id == #uid")
	@RequestMapping(value = "/bleets/{uid}/{bleetId}/{bleet}", method=RequestMethod.PUT)
	public @ResponseBody List<Bleet> updateBleet(@PathVariable String uid, @PathVariable String bleetId,@PathVariable String bleet){
		
		BleeterUser user = userService.findById(uid);
		bleetService.updateBleet(bleetId, bleet);
		
		if (user.getRoles().get(0).equals("ROLE_ADMIN")){
			return bleetService.findAll(new Sort(Direction.ASC, "timestamp"));
		}
		return bleetService.findAllPublicAndUnblocked(new Sort(Direction.ASC, "timestamp"));
	}

	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PreAuthorize(value = "principal.id == #uid")
	@RequestMapping(value = "/bleets/{uid}/{bleetId}", method=RequestMethod.DELETE)
	public @ResponseBody BleeterUser removeBleet(@PathVariable String uid, @PathVariable String bleetId){
		
		bleetService.removeBleet(uid, bleetId);
		
		return userService.findById(uid);
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping(value = "/bleets/{bleetId}/{block}", method=RequestMethod.PUT)
	public @ResponseBody List<Bleet> setBlock(@PathVariable String bleetId, @PathVariable Boolean block){
		
		bleetService.setBlock(bleetId, block);
		
		return bleetService.findAll(new Sort(Direction.ASC, "timestamp"));
	}
	
	
	//TODO: rest-ier path
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/bleets/users/manage", method=RequestMethod.GET)
	public String manageUsers(){
		return "manageUsers";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/bleets/users", method=RequestMethod.POST)
	public @ResponseBody List<BleeterUser> createUser(@RequestParam String username, @RequestParam String password, @RequestParam String firstname, @RequestParam String lastname){
		List<BleeterUser> usrList = userService.findAll();
		
		for(BleeterUser usr : usrList){
			if (usr.getUsername().equalsIgnoreCase(username)){
				//prevents duplicate usernames does not throw error
				return usrList;
			}
		}
		
		return userService.createUser(username, password, firstname, lastname);
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/bleets/users", method=RequestMethod.GET)
	public @ResponseBody List<BleeterUser> getUsers(){
		return userService.findAll();
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping(value = "/bleets/users/{uid}", method=RequestMethod.GET)
	public @ResponseBody BleeterUser getSingleUser(@PathVariable String uid){
		return userService.findById(uid);
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/bleets/users/{uid}/{role}", method=RequestMethod.PUT)
	public @ResponseBody List<BleeterUser> updateRole(@PathVariable String uid, @PathVariable String role){
				
		//workaround so an admin cannot remove their own admin role.  For whatever reason my manageUsers.jsp cannot access EL objects, and
		//@AuthenticationPrincipal crashes even though @EnableWebMvcSecurity is enabled.  Not a good way to do this but it works
		User user2 =  (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user2.getUsername().equals(userService.findById(uid).getUsername())){
			return userService.findAll();
		}
		return userService.updateRole(uid, role);
	}
	
	
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping(value = "/bleets/users/{uid}", method=RequestMethod.POST)
	@PreAuthorize(value = "principal.id == #uid")
	public @ResponseBody BleeterUser updateProfile(@PathVariable String uid, @RequestParam String username, @RequestParam String firstname, @RequestParam String lastname,
													@RequestParam String email, @RequestParam("avatar") MultipartFile file, @RequestParam List<String> favorites) throws IOException{
		
		BleeterUser user = userService.findById(uid);
		
		String av = "";
		if(!file.isEmpty()) {
			av = userService.uploadAvatar(user, file);
		}
		
		userService.updateProfile(uid, username, firstname, lastname, email, av, favorites);
		
		return userService.findById(uid);
	}
	
	
	@RequestMapping(value="/bleets/{uid}/avatar", method=RequestMethod.GET)
	@PreAuthorize("principal.id == #uid")
	public 	@ResponseBody byte[] getAvatar(@PathVariable String uid) throws IOException {		
		return userService.getImageData(uid);	
	}
}
