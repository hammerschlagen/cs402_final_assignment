package com.cs402.bleeter.bleets;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Bleet {
	@Id
	public String id;
	public String ownerId;
	public String owner;
	public String bleet;
	public Date timestamp;
	public String confidence;
	public String sentiment;
	public Boolean isBlocked;
	public Boolean isPrivate;
	
	@PersistenceConstructor
	public Bleet(String id, String ownerId, String owner, String bleet, Date timestamp, String confidence, String sentiment, Boolean isBlocked, Boolean isPrivate){
		this.id = id;
		this.ownerId = ownerId;
		this.owner = owner;
		this.bleet = bleet;
		this.timestamp = timestamp;
		this.confidence = confidence;
		this.sentiment = sentiment;
		this.isBlocked = isBlocked;
		this.isPrivate = isPrivate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBleet() {
		return bleet;
	}

	public void setBleet(String bleet) {
		this.bleet = bleet;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public Boolean getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

}
