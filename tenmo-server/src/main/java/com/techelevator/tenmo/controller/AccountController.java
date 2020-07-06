package com.techelevator.tenmo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

	private TransferDAO transferDao;
	private AccountDAO accountDao;
	private UserDAO userDao;
	
		
	public AccountController(AccountDAO accountDao, TransferDAO transferDao, UserDAO userDao) {
		this.accountDao = accountDao;
		this.transferDao = transferDao;
		this.userDao = userDao;
	}
	
	@RequestMapping(path = "users/{id}/balance", method = RequestMethod.GET)
	public BigDecimal getCurrentBalance(@PathVariable long id) {
		return accountDao.getCurrentBalance(id);
	}
	
	@RequestMapping(path = "users/{userId}/transfers", method = RequestMethod.GET)
	public List<Transfer> getTransferHistory(@PathVariable long userId) {
		return transferDao.getTransferHistory(userId);
	}
	
	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> getListOfUsers() {
		return accountDao.getListOfUsers();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "users/{personalId}/transfers/{userId}/amount/{amount}", method = RequestMethod.POST)
	public void sendBucks(@PathVariable long personalId, @PathVariable long userId, @PathVariable BigDecimal amount, @RequestBody Transfer transfer)  {
		accountDao.sendBucks(transfer);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "users/{personalId}/transfers/request/{userId}/amount/{amount}", method = RequestMethod.POST)
	public void requestBucks(@PathVariable long personalId, @PathVariable long userId, @PathVariable BigDecimal amount, @RequestBody Transfer transfer) {
		accountDao.requestBucks(transfer);
	}
	
	@RequestMapping(path = "users/{personalId}/transfers/pending")
	public List<Transfer> viewPendingRequests(@PathVariable long personalId)
	{
		return transferDao.viewPendingRequests(personalId);
	}
	
	@RequestMapping(path = "users/{personalId}/transfers/{transferId}", method = RequestMethod.PUT)
	public void approveOrRejectTransfer(@PathVariable long transferId, @PathVariable long personalId, @RequestBody Transfer transfer)
	{
		transferDao.approveOrRejectTransfer(transfer);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
