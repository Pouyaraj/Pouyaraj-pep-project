package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDao;

    public AccountService() {
        accountDao = new AccountDAO();
    }

    public Account registerAccount(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return null; 
        }


        if (password == null || password.length() < 4) {
            return null; 
        }

        if (accountDao.isUsernameTaken(username)) {
            return null;
        }


        Account newAccount = new Account();
        newAccount.setUsername(username);
        newAccount.setPassword(password);

        return accountDao.addAccount(newAccount); 
    }

    public Account loginAccount(String username, String password){
        Account account = accountDao.getAccount(username);

        if (account != null && account.getPassword().equals(password)) {
            return account; 
        }
        return null; 
    }
    
}
