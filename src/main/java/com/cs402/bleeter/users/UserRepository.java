package com.cs402.bleeter.users;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<BleeterUser, String>, UpdateableUserRepository{
	public BleeterUser findByUsername(String username);
	
	@Query(value= "{ 'username' : ?0, 'profile.password' : ?1 }" )
	public BleeterUser findByUsernameAndPassword(String username, String password);
}

