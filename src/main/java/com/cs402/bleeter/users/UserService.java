package com.cs402.bleeter.users;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	public BleeterUser findById(String uid) {
		return userRepository.findOne(uid);
	}
	
	public BleeterUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public List<BleeterUser> createUser(String username, String password, String firstName, String lastName){
		//the new user will have to log in and fill out the rest of their profile
		BleeterUser newUser = new BleeterUser.Builder()
									.username(username)
									.profile(new UserProfile.Builder()
													.firstName(firstName)
													.lastName(lastName)
													.username(username)
													.email("")
													.avatar("")
													.password(password)
													.build())
									.build();
		
		List<String> favs = new ArrayList<String>();
		newUser.getProfile().setFavorites(favs);
		
		List<String> r = new ArrayList<String>();
		r.add("ROLE_USER");
		newUser.setRoles(r);
		
		userRepository.insert(newUser);
		return userRepository.findAll();
	}
	
	//TODO:possibly remove
	public void deleteUser(String userId) {
		userRepository.delete(userId);
	}
	
	public List<BleeterUser> findAll() {
		return userRepository.findAll();
	}

	public void updateProfile(String userId, String username, String firstName, String lastName, String email, String avatar, List<String> favorites) {

		BleeterUser user = userRepository.findOne(userId);
		
		user.setUsername(username);
		user.getProfile().setUsername(username);
		user.getProfile().setFirstName(firstName);
		user.getProfile().setLastName(lastName);
		user.getProfile().setEmail(email);
		user.getProfile().setAvatar(avatar);
		user.getProfile().setFavorites(favorites);
		
		userRepository.update(user);
	}
	
	public List<BleeterUser> updateRole(String uid, String role){
		
		BleeterUser user = userRepository.findOne(uid);
		
		List<String> tmp = new ArrayList<String>(1);
		tmp.add(role);
		user.setRoles(tmp);
		
		userRepository.update(user);
		
		return userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("LOADING A USER +++++++++++++++++++++++++++++++++ " + username);
		BleeterUser user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " not found.");
		}
		
		User details = new UserWithId(user.getUsername(), user.getProfile().getPassword(), true, true, true, true, authorities(user.getRoles()), user.getId());
		return details;
	}
	
	public List<GrantedAuthority> authorities(List<String> auths) {
		List<GrantedAuthority> result = new ArrayList<>();
		for (String auth : auths) {
			result.add(new SimpleGrantedAuthority(auth));
		}
		return result;
	}
	
	public String getAvatarPath(String uid) {
		File root = FileSystemView.getFileSystemView().getRoots()[0];
		String path = root.getAbsolutePath() + File.separator + "tmp" + File.separator + "bleeter" + File.separator + uid;
		return path;
	}	

	public String uploadAvatar(BleeterUser user, MultipartFile file) throws IOException {
		
		String avatar = getAvatarPath(user.getId());
		BufferedImage image = ImageIO.read(file.getInputStream());
		ImageIO.write(image, "PNG", new File(avatar));
		
		user.getProfile().setAvatar(avatar);
		
		return avatar;
	}
	

	public File getAvatarFile(String uid) {
		File root = FileSystemView.getFileSystemView().getRoots()[0];
		String path = root.getAbsolutePath() + File.separator + "tmp" + File.separator + "bleeter" + File.separator + uid;
		return new File(path);
	}	

	public byte[] getImageData(String uid) {
		BleeterUser user = findById(uid);

		try {
			if (user.getProfile().getAvatar() != null && user.getProfile().getAvatar().length() > 1) {
				return FileCopyUtils.copyToByteArray(getAvatarFile(uid));
			} else {
				//default if no avatar set
				return FileCopyUtils.copyToByteArray(getAvatarFile("twitter-default-avatar-egg"));
			}
		} catch (Exception e) {
			return null;
		}
	}
}
