package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    public Account save(Account account) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL query to insert a new account with placeholders for username and password
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            // Prepare the SQL statement 
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the values for the placeholders in the SQL query
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            // Execute the insert operation
            ps.executeUpdate();

            // Retrieve the account_id
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                // Extract the generated account_id and return the newly created account object
                int generatedAccountId = rs.getInt("account_id");
                return new Account(generatedAccountId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            // Log the SQL exception details for debugging purposes
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        // Return null if the account could not be saved
        return null;
    }

    public Account findByUsername(String username) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL query to select an account based on the username
            String sql = "SELECT * FROM account WHERE username = ?";
            // Prepare the SQL statement with a placeholder for the username
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set the value for the username placeholder
            ps.setString(1, username);

            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // Iterate through the result set 
            while (rs.next()) {
                // Create an Account object from the retrieved data
                Account account = new Account(
                    rs.getInt("account_id"),  // Account ID
                    rs.getString("username"), // Username
                    rs.getString("password")  // Password
                );
                // Return the account if found
                return account;
            }
        } catch (Exception e) {
            // Log any exception that occurs during the query execution
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        // Return null if no account was found with the given username
        return null;
    }

    public Account findByAccountId(int accountId) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL query to select an account based on the account_id
            String sql = "SELECT * FROM account WHERE account_id = ?";
            // Prepare the SQL statement with a placeholder for the account_Id
            PreparedStatement ps = connection.prepareStatement(sql);
            // Set the value for the account_id placeholder
            ps.setInt(1, accountId);

            // Execute the query
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Map the result set to an Account object
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            // Handle database exceptions
            e.printStackTrace();
        }
        return null;
    }

}
