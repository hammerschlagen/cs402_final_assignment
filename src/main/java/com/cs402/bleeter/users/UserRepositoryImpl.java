package com.cs402.bleeter.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UserRepositoryImpl implements UpdateableUserRepository {
	@Autowired
	@Qualifier("defaultMongoTemplate")
	private MongoOperations mongo;
		
	private Update getUpdate(BleeterUser x, BleeterUser y) {
		Update update = new Update();
		update.set("username", y.getUsername());
		update.set("profile.firstName", y.getProfile().getFirstName());
		update.set("profile.lastName", y.getProfile().getLastName());
		update.set("profile.username", y.getProfile().getUsername());
		update.set("profile.email", y.getProfile().getEmail());
		update.set("profile.avatar", y.getProfile().getAvatar());
		update.set("profile.favorites", y.getProfile().getFavorites());
		update.set("profile.password", y.getProfile().getPassword());
		update.set("roles", y.getRoles());
		return update;
	}
	
	@Override
	public BleeterUser update(BleeterUser user) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(user.getUsername()));
		BleeterUser old = mongo.findOne(query,  BleeterUser.class);		
		mongo.updateFirst(query, getUpdate(old, user), BleeterUser.class);
		return mongo.findOne(query, BleeterUser.class);
	}
}