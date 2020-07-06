package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;


public class AccountService {
	
	  public static String AUTH_TOKEN = "";
	  private final String BASE_URL;
	  private final RestTemplate restTemplate = new RestTemplate();
	
	  public AccountService(String url) {
	        this.BASE_URL = url;
	  }
	  
	 
	  public BigDecimal getCurrentBalance(long id) {
		  
			BigDecimal balance = null;
			
			try {
				
				balance = restTemplate.exchange(BASE_URL + "users/" + id + "/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
				
			} catch (RestClientResponseException ex) {
				
				System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			}
			
			return balance;	
	}
	  
	  
	  public Transfer[] getTransferHistory(long userId) {
		  
		  Transfer[] transfers = null;
		  
		  try {
			 
			  transfers = restTemplate.exchange(BASE_URL + "users/" + userId + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		
		  } catch (RestClientResponseException ex) {
		
			  System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		  }
		  
		  return transfers;  
	  }
	
	 
	  public User[] getListOfUsers() {
		  
		  User[] users = null;
		  
		  try {
			  users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		
		  } catch (RestClientResponseException ex) {
			  
			  System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		  }
		   
		  return users;
	  }
	  
	  public void sendBucks(long personalId, long userId, BigDecimal amount) {
		  Transfer transfer = new Transfer();
		  transfer.setAccountFrom(personalId);
		  transfer.setAccountTo(userId);
		  transfer.setAmount(amount);
		  try {
			  restTemplate.postForObject(BASE_URL + "users/" + personalId + "/transfers/" + userId + "/amount/" + amount, makeTransferEntity(transfer), Transfer.class);		 
			  System.out.println("Transfer completed");
		  } catch (RestClientResponseException ex) {
			  System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			  
		  } catch (ResourceAccessException ex) {
			  System.out.println(ex.getMessage());
			  
		  }
		  
	  }
	  
	  public void requestBucks(long personalId, long userId, BigDecimal amount)
	  {
		  Transfer transfer = new Transfer(); 
		  transfer.setAccountFrom(userId);
		  transfer.setAccountTo(personalId);
		  transfer.setAmount(amount);
		  
		  try {
			  restTemplate.postForObject(BASE_URL + "users/" + personalId + "/transfers/request/" + userId + "/amount/" + amount, makeTransferEntity(transfer), Transfer.class);
			  System.out.println("Successfully requested funds");
		  }
		  catch(RestClientResponseException ex) {
			  System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());  
		  } catch (ResourceAccessException ex) {
			  System.out.println(ex.getMessage());
		  }
		  
	  }
	  
	  public Transfer[] viewPendingRequests(long personalId)
	  {
		  Transfer[] transfers = null;
		  
		  try {
			  transfers = restTemplate.exchange(BASE_URL + "users/" + personalId + "/transfers/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		  } catch (RestClientResponseException ex) {
			  
			  System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		  }
		  return transfers;
	  }
	  
	  public void approveOrRejectTransfer(Transfer transfer, long personalId)
	  {
		  try {
			  restTemplate.put(BASE_URL + "users/" + personalId + "/transfers/" + transfer.getTransferId(), makeTransferEntity(transfer), Transfer.class);		 
			  if (transfer.getTransferStatusId() == 2)
			  {
				  System.out.println("Transfer Approved!");
			  }
			  else if (transfer.getTransferStatusId() == 3)
			  {
				  System.out.println("Transfer Rejected!");
			  }
		  } catch (RestClientResponseException ex) {
			  System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			  
		  } catch (ResourceAccessException ex) {
			  System.out.println(ex.getMessage());		  
		  }
		  
	  }
	  
	
	  private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
	  	headers.setContentType(MediaType.APPLICATION_JSON);
	  	headers.setBearerAuth(AUTH_TOKEN);
	  	HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	 }

	  private HttpEntity makeAuthEntity() {
		 HttpHeaders headers = new HttpHeaders();
		 headers.setBearerAuth(AUTH_TOKEN);
		 HttpEntity entity = new HttpEntity<>(headers);
		 return entity;
	  }

}
