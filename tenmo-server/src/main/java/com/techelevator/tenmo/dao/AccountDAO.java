package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public interface AccountDAO {
	
	BigDecimal getCurrentBalance(long id);
	
	List<User> getListOfUsers();
	
	void sendBucks(Transfer transfer);	
	
	void requestBucks(Transfer transfer);

	
}