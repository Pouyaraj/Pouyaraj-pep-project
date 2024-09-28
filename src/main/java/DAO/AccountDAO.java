package DAO;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the values for the prepared statement
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            // Execute the update
            preparedStatement.executeUpdate();

            // Get the generated account_id
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generatedAccountId = pkeyResultSet.getInt(1);
                
                // Return the new Account object with the generated account_id
                return new Account(generatedAccountId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If count is greater than 0, the username is taken
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean userExists(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT account_id FROM Account WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Returns true if an account with the given ID exists
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Account getAccount(String username){
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int generatedAccountId = resultSet.getInt("account_id");
                    String password = resultSet.getString("password");
                    return new Account(generatedAccountId, username, password);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
