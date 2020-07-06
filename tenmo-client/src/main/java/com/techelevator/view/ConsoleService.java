package com.techelevator.view;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch (NumberFormatException e) {
				out.println("\n*** " + userInput + " is not valid ***\n");
			}
		} while(result == null);
		
		return result;
	}
	
	public Long getTransferId(List<Long> transferIds) {
	
		long transferId = 0;
		try {
			transferId = (long) Integer.parseInt(in.nextLine()); 
		}
		catch(NumberFormatException ex) {
			System.out.println("Invalid number format.");
			return transferId;
		}
	 
	 while(!transferIds.contains(transferId) && transferId != 0) {
		 
		 System.out.println("Invalid input. Please enter a valid transfer id.");
		 
		 transferId = (long) Integer.parseInt(in.nextLine()); 
	 }
	 
	 return transferId; 
	
	}
	
	public List<String> getUserAndAmountToSend(User[] users, long personalId) { 
		
		List<String> userAndAmountToSend = new ArrayList<String>();
		List<Long> userIds = new ArrayList<Long>();
		
	
		for (User user : users) {
			
			userIds.add((long) user.getId());
		}
		
		 System.out.println("Enter ID of user you are sending to (0 to cancel): ");
		
			String id = in.nextLine();
			long idAsLong = 0;
			try{
				idAsLong = (long) Integer.parseInt(id);
			}
			catch(NumberFormatException ex)
			{
				System.out.println("Invalid number format.");
				return userAndAmountToSend;
			}
			
			while (!userIds.contains(idAsLong) && idAsLong != 0) {
				System.out.println("Invalid id.");
				System.out.println("Enter ID of user you are sending to (0 to cancel): ");
				id = in.nextLine();
				try{
					idAsLong = (long) Integer.parseInt(id);
				}
				catch(NumberFormatException ex)
				{
					System.out.println("Invalid number format.");
					return userAndAmountToSend;
				}
			}
			
			if(idAsLong == 0) {
				return userAndAmountToSend;	
			}
			
			System.out.println("Enter amount: ");
		
		 	String amount = in.nextLine();
		 	BigDecimal amountAsBigDecimal = new BigDecimal(0);
		 	try
		 	{
		 		amountAsBigDecimal = new BigDecimal(amount);
		 	}
		 	catch(NumberFormatException ex)
		 	{
		 		System.out.println("Invalid number format");
		 		return userAndAmountToSend;
		 	}
			BigDecimal userBalance = new BigDecimal(0);
			personalId = 3;
			
			for (User user: users) {				
				if (user.getId() == personalId) {
					
					userBalance = user.getUserAccountBalance();
					break;
				}
			}
			
			while (userBalance.compareTo(amountAsBigDecimal) == -1) {
				System.out.println("I'm sorry. You only have $" + userBalance + " in your account. Please enter a new amount.");
				amount = in.nextLine();
				try {
				amountAsBigDecimal = new BigDecimal(amount);
				}
				catch (NumberFormatException ex)
				{
					System.out.println("Invalid number format");
					return userAndAmountToSend;
				}
			}
			
			userAndAmountToSend.add(id);
			userAndAmountToSend.add(amount);
			
		return userAndAmountToSend;
		
	}
	
public List<String> getUserAndAmountToRequest(User[] users, long personalId) {
		
		List<String> userAndAmountToRequest = new ArrayList<String>();
		List<Long> userIds = new ArrayList<Long>();
		
		for (User user : users) {
			
			userIds.add((long) user.getId());
		}
		
		 System.out.println("Enter ID of user you are requesting from (0 to cancel): ");
		
			String id = in.nextLine();
			long idAsLong = 0;
			try{
				idAsLong = (long) Integer.parseInt(id);
			}
			catch(NumberFormatException ex)
			{
				System.out.println("Invalid number format.");
				return userAndAmountToRequest;
			}
			
			while (!userIds.contains(idAsLong) && idAsLong != 0) {
				System.out.println("Invalid id.");
				System.out.println("Enter ID of user you are requesting from (0 to cancel): ");
				id = in.nextLine();
				try{
					idAsLong = (long) Integer.parseInt(id);
				}
				catch(NumberFormatException ex)
				{
					System.out.println("Invalid number format.");
					return userAndAmountToRequest;
				}
			}
			
			if(idAsLong == 0) {
				return userAndAmountToRequest;	
			}
			
			System.out.println("Enter amount: ");
		
		 	String amount = in.nextLine();
		 	BigDecimal amountAsBigDecimal = new BigDecimal(0);
		 	try
		 	{
		 		amountAsBigDecimal = new BigDecimal(amount);
		 	}
		 	catch(NumberFormatException ex)
		 	{
		 		System.out.println("Invalid number format");
		 		return userAndAmountToRequest;
		 	}
			BigDecimal userBalance = new BigDecimal(0);
			personalId = 3;
			
			for (User user: users) {				
				if (user.getId() == personalId) {
					userBalance = user.getUserAccountBalance();
				}
			}
			
			while (userBalance.compareTo(amountAsBigDecimal) == -1) {
				System.out.println("I'm sorry. You only have $" + userBalance + " in your account. Please enter a new amount.");
				amount = in.nextLine();
				try {
				amountAsBigDecimal = new BigDecimal(amount);
				}
				catch (NumberFormatException ex)
				{
					System.out.println("Invalid number format");
					return userAndAmountToRequest;
				}
			}
			
			userAndAmountToRequest.add(id);
			userAndAmountToRequest.add(amount);
			
		return userAndAmountToRequest;
		
	}

	public Transfer approveOrRejectTransfer(Transfer[] transfers, List<Long> transferIds)
	{
		Transfer transfer = new Transfer();
		
		System.out.println("Please enter transfer ID to approve/reject (0 To cancel): ");
		String userInput = in.nextLine();
		long transferId = 0;
		try
		{
			transferId = (long) Integer.parseInt(userInput);
		}
		catch(NumberFormatException ex)
		{
			System.out.println("Invalid number format");
			return transfer;
		}
		
		if (transferId == 0)
		{
			transfer.setTransferStatusId(1);
			return transfer;
		}
		
		while (!transferIds.contains(transferId) && transferId != 0)
		{
			System.out.println("Invalid response. Please enter a valid ID or 0: ");
			userInput = in.nextLine();
			try
			{
				transferId = (long) Integer.parseInt(userInput);
			}
			catch(NumberFormatException ex)
			{
				System.out.println("Invalid number format");
				return transfer;
			}
		}
		
		System.out.println("1: Approve");
		System.out.println("2: Reject");
		System.out.println("0: Don't approve or reject");
		System.out.println("---------");
		System.out.println("Please choose an option: ");
		
		String approveRejectNeither = in.nextLine();
		
		while (!approveRejectNeither.equals("0") && !approveRejectNeither.equals("1") && !approveRejectNeither.equals("2"))
		{
			System.out.println("Invalid response. Please choose 0, 1, or 2: ");
			approveRejectNeither = in.nextLine();
		}

		
		for (Transfer transfer1: transfers)
		{
			if (transfer1.getTransferId() == transferId)
			{
				transfer.setTransferId(transferId);
				transfer.setAccountFrom(transfer1.getAccountFrom());
				transfer.setAccountTo(transfer1.getAccountTo());
				transfer.setAmount(transfer1.getAmount());
				if (approveRejectNeither.equals("1"))
				{
					transfer.setTransferStatusId(2);
				}
				else if (approveRejectNeither.equals("2"))
				{
					transfer.setTransferStatusId(3);
				}
				else
				{
					transfer.setTransferStatusId(transfer1.getTransferStatusId());
				}
				transfer.setTransferTypeId(transfer1.getTransferTypeId());
			}
		}
		return transfer;
	}
}