package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	
	private long transferId;
	private int transferTypeId;
	private int transferStatusId;
	private long accountFrom;
	private long accountTo;
	private BigDecimal amount;
	private String accountFromName;
	private String accountToName;
	
	
	public String getAccountFromName() {
		return accountFromName;
	}
	public void setAccountFromName(String accountFromName) {
		this.accountFromName = accountFromName;
	}
	public String getAccountToName() {
		return accountToName;
	}
	public void setAccountToName(String accountToName) {
		this.accountToName = accountToName;
	}
	public long getTransferId() {
		return transferId;
	}
	public void setTransferId(long transferId) {
		this.transferId = transferId;
	}
	public int getTransferTypeId() {
		return transferTypeId;
	}
	public void setTransferTypeId(int transferTypeId) {
		this.transferTypeId = transferTypeId;
	}
	public int getTransferStatusId() {
		return transferStatusId;
	}
	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
	}
	public long getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(long accountFrom) {
		this.accountFrom = accountFrom;
	}
	public long getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(long accountTo) {
		this.accountTo = accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	

}