package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TenmoService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TenmoService tenmoService = new TenmoService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        BigDecimal balance = tenmoService.getBalance(currentUser.getUser().getId());
        consoleService.printBalance(balance);
        }

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		List<String> transfers = tenmoService.getTransfers(currentUser.getUser().getId());
        consoleService.printTransfers(transfers);
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        List<String> requests = tenmoService.getPendingRequests();
        consoleService.printPendingRequests(requests);
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        List<String> allUsers = tenmoService.getAllUsers();
        consoleService.printAllUsers(allUsers);
        int toUserId = consoleService.promptForInt("User ID of the person you're sending to: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Amount to send: $");
        System.out.println();
        viewCurrentBalance();

    }

	private void requestBucks() {
		// TODO Auto-generated method stub
        List<String> allUsers = tenmoService.getAllUsers();
        consoleService.printAllUsers(allUsers);
        int fromUserId = consoleService.promptForInt("User ID of the person you're requesting from: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Amount to request: $");

		
	}

}
