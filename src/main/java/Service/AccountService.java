package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDao;

    // Constructor to inject the DAO
    public AccountService() {
        accountDao = new AccountDAO();
    }

    // Method to register a new account
    public Account registerAccount(String username, String password) {
        // Validate username is not blank
        if (username == null || username.trim().isEmpty()) {
            return null; // Will trigger a 400 response in the controller
        }

        // Validate password length is at least 4 characters
        if (password == null || password.length() < 4) {
            return null; // Will trigger a 400 response in the controller
        }

        // Check if username already exists
        if (accountDao.isUsernameTaken(username)) {
            return null; // Will trigger a 400 response in the controller
        }

        // Create a new account and save it to the database
        Account newAccount = new Account();
        newAccount.setUsername(username);
        newAccount.setPassword(password);

        return accountDao.addAccount(newAccount); // Persist the account
    }

    public Account loginAccount(String username, String password){
        Account account = accountDao.getAccount(username);

           // Check if the account exists and if the passwords match
        if (account != null && account.getPassword().equals(password)) {
            return account; // Successful login, return the account
        }
        return null; // Login failed
    }
    
}
