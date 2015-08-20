package com.cs402.bleeter.users;

import java.util.List;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class UserProfile {
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String avatar;
	private List<String> favorites;
	@JsonIgnore
	private String password;
	
	@PersistenceConstructor
	public UserProfile(String firstName, String lastName, String username, String email, String avatar, List<String> favorites, String password){
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.avatar = avatar;
		this.favorites = favorites;
		this.password = password;
	}
	
	public UserProfile(){}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<String> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<String> favorites) {
		this.favorites = favorites;
	}

	public UserProfile(Builder b) {
		this.avatar = b.avatar;
		this.email = b.email;
		this.favorites = b.favorites;
		this.firstName = b.firstName;
		this.lastName = b.lastName;
		this.username = b.username;
		this.password = b.password;
	}

	public static class Builder {
		private String avatar;
		private String firstName;
		private String lastName;
		private String email;
		private List<String> favorites;
		private String username;
		private String password;

		public Builder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder favorites(List<String> favorites) {
			this.favorites = favorites;
			return this;
		}

		public UserProfile build() {
			return new UserProfile(this);
		}

	}
}
