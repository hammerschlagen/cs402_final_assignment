package com.cs402.bleeter.bleets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cs402.bleeter.users.BleeterUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BleetService {
	@Autowired
	BleetRepository bleetRepo;
	
	public List<Bleet> findAll(Sort s){
		return bleetRepo.findAll(s);
	}
	
	public List<Bleet> findAllPublicAndUnblocked(Sort s){
		return bleetRepo.findAllPublicAndUnblocked(false, false, s);
	}
	
	public List<Bleet> findAllByOwnerId(String uid, Sort s){
		return bleetRepo.findAllByOwnerId(uid, s);
	}
	
	public Boolean createBleet(BleeterUser user, String bleet, Boolean isPriv) throws URISyntaxException, ClientProtocolException, IOException{

		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://sentiment.vivekn.com/api/text/");
		
		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("txt", bleet));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		//Execute and get the response.
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		
		String confidence = "";
		String sentiment = "";
		
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    try {
		    	ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(instream);
				
				JsonNode results = root.get("result");
				confidence = results.get("confidence").asText();
				sentiment = results.get("sentiment").asText();
				
				System.out.println(confidence);
				System.out.println(sentiment);
		    } finally {
		        instream.close();
		    }
		}
		
		//TODO: builder pattern
		Bleet newBleet = new Bleet(UUID.randomUUID().toString(), user.getId(), user.getUsername(), bleet, new Date(), confidence, sentiment, false, isPriv);
		bleetRepo.save(newBleet);
		
		Bleet successfulSave = bleetRepo.findOne(newBleet.id);
		if (successfulSave != null){
			return true;
		}
		return false;
	}
	
	public Boolean removeBleet(String uid, String bleetId){
		
		Bleet tmp = bleetRepo.findOne(bleetId);
		
		if (tmp.ownerId.equals(uid)){ 
			bleetRepo.delete(bleetRepo.findOne(bleetId));
			return true;
		}
		return false;
	}
	
	public Boolean updateBleet(String bleetId, String bleet){
		
		Bleet tmp = bleetRepo.findOne(bleetId);
		tmp.setBleet(bleet);
		
		//TODO: update bleet sentiment ratings based on new text; low priority
		bleetRepo.update(tmp);
		return true;
	}

	public Boolean setBlock(String bleetId, Boolean block){
		
		Bleet tmp = bleetRepo.findOne(bleetId);
		tmp.setIsBlocked(block);
		bleetRepo.update(tmp);
		return true;
	}
	
	public List<Bleet> search(String search, Sort s) {
		return bleetRepo.findAllByOwnerLikeOrTimestampLike(search, search, s);
	}

	public List<Bleet> nonAdminSearch(String search, Sort s) {
		return bleetRepo.findAllByOwnerLikeOrTimestampAndIsBlockedAndIsPrivate(search, search, false, false, s);
	}
}
