package com.cs402.bleeter.users;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class BleeterUser {

	@Id
	private String id;
	@Indexed
	private String username;
	private UserProfile profile;
	//ideally i think this should be ignored, but it's needed for user management
	private List<String> roles;
	
	@PersistenceConstructor
	public BleeterUser(String id, String username, UserProfile profile, List<String> roles){
		this.id = id;
		this.username = username;
		this.profile = profile;
		this.roles = roles;
	}
	
	public BleeterUser(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private BleeterUser (Builder b){
		this.id = b.id;
		this.profile = b.profile;
		this.roles = b.roles;
		this.username = b.username;
	}
	
	public static class Builder {
		private String id;
		private UserProfile profile;
		private List<String> roles;
		private String username;
		
		public Builder id(String id) {
			this.id = id;
			return this;
		}
		
		public Builder roles(List<String> roles) {
			this.roles = roles;
			return this;
		}		
		
		public Builder username(String username) {
			this.username= username;
			return this;
		}
		
		public Builder profile(UserProfile profile) {
			this.profile = profile;
			return this;
		}
		
		public BleeterUser build() {
			return new BleeterUser(this);
		}
	}
}
