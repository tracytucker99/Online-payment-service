package com.techelevator.tenmo.models;

import java.math.BigDecimal;
public class User {

	private Long id;
	private String username;
	private BigDecimal userAccountBalance;
	   
	   
	public BigDecimal getUserAccountBalance() {
		return userAccountBalance;
	}

	public void setUserAccountBalance(BigDecimal userAccountBalance) {
		this.userAccountBalance = userAccountBalance;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}