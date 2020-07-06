package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class TransferSqlDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;

	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	

	@Override
	public List<Transfer> getTransferHistory(long userId) {
		
		List<Transfer> transfers = new ArrayList<Transfer>();

		String sql = "SELECT * " +
					 "FROM transfers " +
					 "JOIN accounts ON accounts.account_id = transfers.account_from " +
					 "JOIN users ON users.user_id = accounts.user_id " +
					 "WHERE users.user_id = ?";
				
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		
		while(results.next()) {
			transfers.add(mapRowToTransfer(results));
		}
			   sql = "SELECT * " +
				     "FROM transfers " +
				     "JOIN accounts ON accounts.account_id = transfers.account_to " +
				     "JOIN users ON users.user_id = accounts.user_id " +
				     "WHERE users.user_id = ?";
	
		results = jdbcTemplate.queryForRowSet(sql, userId);
	
		while(results.next()) {
			transfers.add(mapRowToTransfer(results));
		}
		
		return transfers;
	}
	
	@Override
	public List<Transfer> viewPendingRequests(long personalId)
	{
		List<Transfer> transfersRequests = new ArrayList<Transfer>();
		
		String sqlGetTransferRequests = "SELECT * " +
										"FROM transfers " +
										"WHERE account_from = ? AND transfer_status_id = 1";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferRequests, personalId);
		while (results.next())
		{
			transfersRequests.add(mapRowToTransfer(results));
		}
				
		return transfersRequests;
	}
	
	@Override
	public void approveOrRejectTransfer(Transfer transfer)
	{
		String sqlApproveOrRejectTransfer = "UPDATE transfers " +
											"SET transfer_status_id = ? " +
											"WHERE transfer_id = ?";
		
				jdbcTemplate.update(sqlApproveOrRejectTransfer, transfer.getTransferStatusId(), transfer.getTransferId());
	if (transfer.getTransferStatusId() == 2)
		{
		String sqlSendBucks = "UPDATE accounts " +
		          "SET balance = balance + ? " +
		          "WHERE user_id = ?";

				jdbcTemplate.update(sqlSendBucks, transfer.getAmount(), transfer.getAccountTo());
		
		String sqlLoseBucks =  "UPDATE accounts " +
			   				"SET balance = balance - ? " +
			   				"WHERE user_id = ?";

				jdbcTemplate.update(sqlLoseBucks, transfer.getAmount(), transfer.getAccountFrom());
		}
	}
	
	private Transfer mapRowToTransfer(SqlRowSet results) {
		
		Transfer transfer = new Transfer();
		
		transfer.setTransferId(results.getLong("transfer_id"));
		transfer.setTransferTypeId(results.getInt("transfer_type_id"));
		transfer.setTransferStatusId(results.getInt("transfer_status_id"));
		transfer.setAccountFrom(results.getLong("account_from"));
		transfer.setAccountTo(results.getLong("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		
		String sqlGetName = "SELECT users.username " +
					        "FROM users " +
					        "JOIN accounts ON accounts.user_id = users.user_id " +
					        "WHERE accounts.account_id = ?";
		
		SqlRowSet nameResults = jdbcTemplate.queryForRowSet(sqlGetName, transfer.getAccountFrom());
		
		while (nameResults.next()) {
			transfer.setAccountFromName(nameResults.getString("username"));
		}
		
			  sqlGetName = "SELECT users.username " +
					       "FROM users " +
				           "JOIN accounts ON accounts.user_id = users.user_id " +
				           "WHERE accounts.account_id = ?";
		
		nameResults = jdbcTemplate.queryForRowSet(sqlGetName, transfer.getAccountTo());
		
		while (nameResults.next()) {
			transfer.setAccountToName(nameResults.getString("username"));
		}
						
		return transfer;
	}

}