package com.cs402.bleeter.bleets;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BleetRepository extends MongoRepository<Bleet, String>, UpdateableBleetRepository, PagingAndSortingRepository<Bleet, String>{

	@Query(value= "{ 'isPrivate' : ?0, 'isBlocked' : ?1 }" )
	public List<Bleet> findAllPublicAndUnblocked(Boolean isPriv, Boolean isBlocked, Sort sort);
	
	public List<Bleet> findAllByOwnerId(String ownerId, Sort sort);
	
	public List<Bleet> findAllByOwner(String username, Sort sort);
	
	public List<Bleet> findAllByOwnerLikeOrTimestampLike(String username, String timestamp, Sort sort);
	
	public List<Bleet> findAllByOwnerLikeOrTimestampAndIsBlockedAndIsPrivate(String username, String timestamp, Boolean isPrivate, Boolean isBlocked, Sort sort);

}
