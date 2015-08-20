package com.cs402.bleeter.bleets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class BleetRepositoryImpl implements UpdateableBleetRepository {
	@Autowired
	@Qualifier("defaultMongoTemplate")
	private MongoOperations mongo;
	
	public Update getUpdate(Bleet x, Bleet y){	
		Update update = new Update();
		update.set("bleet", y.getBleet());
		update.set("isPrivate", y.getIsPrivate());
		update.set("isBlocked", y.getIsBlocked());
		return update;
	}

	@Override
	public Bleet update(Bleet bleet) {
		Query query = new Query();
		//change to id?
		query.addCriteria(Criteria.where("_id").is(bleet.getId()));
		Bleet old = mongo.findOne(query,  Bleet.class);		
		mongo.updateFirst(query, getUpdate(old, bleet), Bleet.class);
		return mongo.findOne(query, Bleet.class);
	}

}
