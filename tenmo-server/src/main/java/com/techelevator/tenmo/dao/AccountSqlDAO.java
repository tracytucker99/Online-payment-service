package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@Component
public class AccountSqlDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	private UserSqlDAO userDao;

	public AccountSqlDAO(JdbcTemplate jdbcTemplate, UserSqlDAO userDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.userDao = userDao;
	}	

	@Override
	public BigDecimal getCurrentBalance(long id) {
		
		BigDecimal balance = new BigDecimal(0);
		
		String sql = "SELECT accounts.balance " +
					 "FROM accounts " +
					 "WHERE user_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		
		while (results.next()) {
			
			balance = results.getBigDecimal("balance");
		}
		
		return balance;
	}

	
	@Override
	public List<User> getListOfUsers() {
		
		List<User> listOfUsers = new ArrayList<User>();

		String sql = "SELECT users.user_id, users.username, accounts.balance as balance " +
					 "FROM users " +
					 "JOIN accounts ON accounts.user_id = users.user_id";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		
		while (results.next()) {
			listOfUsers.add(mapRowToUser(results));		
		}
		
		return listOfUsers;
	}


	@Override
	public void sendBucks(Transfer transfer) {
		
		String sqlSendBucks = "UPDATE accounts " +
					          "SET balance = balance + ? " +
					          "WHERE user_id = ?";

		jdbcTemplate.update(sqlSendBucks, transfer.getAmount(), transfer.getAccountTo());
		
		String sqlLoseBucks =  "UPDATE accounts " +
	 			   				"SET balance = balance - ? " +
	 			   				"WHERE user_id = ?";

		jdbcTemplate.update(sqlLoseBucks, transfer.getAmount(), transfer.getAccountFrom());
		
		String sqlInsertIntoTransfer = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
									   "VALUES(2, 2, ?, ?, ?)";
		jdbcTemplate.update(sqlInsertIntoTransfer, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
	}
	
	@Override
	public void requestBucks(Transfer transfer)
	{
		String sqlRequestBucks = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
								 "VALUES(1, 1, ?, ?, ?)";
		jdbcTemplate.update(sqlRequestBucks, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
	}

	
	private User mapRowToUser(SqlRowSet results) {
		
		User user = new User();
		
		user.setId(results.getLong("user_id"));
		user.setUsername(results.getString("username"));
		user.setUserAccountBalance(new BigDecimal(results.getDouble("balance")));
		
		return user;
	}
}