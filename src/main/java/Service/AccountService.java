package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    // map AccountDAO
    AccountDAO accountDAO;

    // No-args constructor for an accountService instantiates a plain AccountDAO
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an accountService when an accountDAO is provided.
     * This is used for when a mock accountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AccountService independently of AccountDAO.
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /*
     * DONE: Use the AccountDAO to add (register) a new account to the database.
     * 
     * This method should also return the added account.
     * 
     * @param account an object representing a new Account.
     * @return the newly added account if the add operation was successful
     */
    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    /*
     * DONE: Use the AccountDAO to login an existing account to the database.
     * 
     * This method should also return the account to login.
     * 
     * @param account an object representing an existing Account.
     * @return the matching account if the login operation was successful
     */
    public Account loginAccount(Account account){
        return accountDAO.loginAccount(account);
    }
}
