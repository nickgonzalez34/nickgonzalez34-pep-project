package Service;

import DAO.AccountDAO;
import Model.Account;

public class LoginService {
    private AccountDAO accountDAO;

    public LoginService() {
        accountDAO = new AccountDAO();
    }

    public Account authenticateLogin(String username, String password) {
        // Fetch the account from the database using the provided username
        Account account = accountDAO.findByUsername(username);
        // If an account with the given username exists and password exists, return account object
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        // Return null if the login fails (invalid username or password)
        return null;
    }
}
