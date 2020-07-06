package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		
		BigDecimal balance = accountService.getCurrentBalance(currentUser.getUser().getId());
		System.out.println("Your current account balance is: $" + balance);
	}

	private void viewTransferHistory() {
		
		Transfer[] transfers = accountService.getTransferHistory(currentUser.getUser().getId());
		List<Long> transferIds = new ArrayList<Long>();
		
		System.out.println("-------------------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-11s%-22s%-9s\n", "ID", "From/To", "Amount");
		System.out.println("-------------------------------------------");
		
		for (Transfer transfer: transfers) {
			
			if(transfer.getAccountFromName().equalsIgnoreCase(currentUser.getUser().getUsername())) {
				System.out.printf("%-11s%-22s%-9s\n", transfer.getTransferId(), "To: " + transfer.getAccountToName(), "$ " + transfer.getAmount());
			}
			else {
				System.out.printf("%-11s%-22s%-9s\n", transfer.getTransferId(), "From: " + transfer.getAccountFromName(), "$ " + transfer.getAmount());	
			}
			
			transferIds.add(transfer.getTransferId());
		}
		
		System.out.println("-------------------------------------------");
		System.out.println("Please enter transfer ID to view details (0 to cancel): ");
		
		long transferId = console.getTransferId(transferIds);
		
		if (transferId != 0) {
			
			System.out.println("-------------------------------------------");
			System.out.println("Transfer Details");
			System.out.println("-------------------------------------------");
			
			for (Transfer transfer: transfers) {
				
				if (transfer.getTransferId() == transferId) {
					System.out.println("Id: " + transfer.getTransferId());
					System.out.println("From: " + transfer.getAccountFromName());
					System.out.println("To: " + transfer.getAccountToName());
					
					if (transfer.getTransferTypeId() == 1) {
						System.out.println("Type: Request");
					}
					else {
						System.out.println("Type: Send");
					}
					
					if (transfer.getTransferStatusId() == 1) {
						System.out.println("Status: Pending");
					}
					else if (transfer.getTransferStatusId() == 2) {
						System.out.println("Status: Approved");
					}
					else {
						System.out.println("Status: Rejected");
					}
					
					System.out.println("Amount: $" + transfer.getAmount());
				}
			}
		}
	}

	private void viewPendingRequests() {
		System.out.println("-------------------------------------------");
		System.out.println("Pending Transfers");
		System.out.printf("%-12s%-22s%-9s\n", "ID", "To", "Amount");
		System.out.println("-------------------------------------------");	
		
		Transfer[] transfers = accountService.viewPendingRequests(currentUser.getUser().getId());
		List<Long> transferIds = new ArrayList<Long>();
		
		for (Transfer transfer: transfers)
		{
			transferIds.add(transfer.getTransferId());
			System.out.printf("%-12s%-22s%-9s\n", transfer.getTransferId(), transfer.getAccountToName(), "$ " + transfer.getAmount());
		}
		System.out.println("-------------------------------------------");	
		Transfer transfer = console.approveOrRejectTransfer(transfers, transferIds);
		if (transfer.getTransferStatusId() != 1)
		{
			accountService.approveOrRejectTransfer(transfer, currentUser.getUser().getId());
		}
	}

	private void sendBucks() {
		
		User[] users = accountService.getListOfUsers();
		
		System.out.println("-------------------------------------------");
		System.out.println("Users");
		System.out.printf("%-12s%-22s\n", "ID", "Name");
		System.out.println("-------------------------------------------");
		
		for (User user : users) {
			
			System.out.printf("%-12s%-22s%s\n", user.getId(), user.getUsername(), user.getUserAccountBalance());
			
		}
		
		System.out.println("-------------------------------------------");
		
		List <String> userAndAmountToSend = console.getUserAndAmountToSend(users, currentUser.getUser().getId());
		if (userAndAmountToSend.size() > 0)
		{
			long userId = (long) Integer.parseInt(userAndAmountToSend.get(0));
			BigDecimal amount = new BigDecimal(userAndAmountToSend.get(1));
			accountService.sendBucks(currentUser.getUser().getId(), userId, amount);
		}
	}

	private void requestBucks() {
		User[] users = accountService.getListOfUsers();

		System.out.println("-------------------------------------------");
		System.out.println("Users");
		System.out.printf("%-12s%-22s\n", "ID", "Name");
		System.out.println("-------------------------------------------");
		
		for (User user : users) {
			
			System.out.printf("%-12s%-22s\n", user.getId(), user.getUsername());			
		}
		System.out.println("-------------------------------------------");
		List <String> userAndAmountToRequest = console.getUserAndAmountToRequest(users, currentUser.getUser().getId());
		if (userAndAmountToRequest.size() > 0)
		{
			long userId = (long) Integer.parseInt(userAndAmountToRequest.get(0));
			BigDecimal amount = new BigDecimal(userAndAmountToRequest.get(1));
			accountService.requestBucks(currentUser.getUser().getId(), userId, amount);
		}
	}
	
	private void exitProgram() {
		System.out.println("Goodbye!");
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
