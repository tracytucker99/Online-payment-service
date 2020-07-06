package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	
	List<Transfer> getTransferHistory(long userId);
	
	List<Transfer> viewPendingRequests(long personalId);

	void approveOrRejectTransfer(Transfer transfer);	
	

}
