package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account register(Account account) {
        // Check if the username is null or blank
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Must enter a username."); 
        }

        // Check if the username already exists in the database
        if (accountDAO.findByUsername(account.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists."); 
        }

        // Check if the password is null or less than 4 characters
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters."); 
        }
        // Save the account to the database and return the saved object
        return accountDAO.save(account);
    }

    public Account findByAccountId(int accountId) {
        // Check if the account Id is null. If so return null
        if (accountDAO.findByAccountId(accountId) == null) {
            return null;
        }
        // Return the account Id
        return accountDAO.findByAccountId(accountId);
    }
}
