package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message saveMessage(Message message) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL query to insert a new message
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            // Prepare the SQL statement 
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the values for the placeholders in the SQL query
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            // Execute the insert operation
            ps.executeUpdate();

            // Retrieve the message_id
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                // Extract the generated message_id and return the newly created message object
                int generatedMessageId = rs.getInt("message_id");
                return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            // Log the SQL exception details for debugging purposes
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        // Return null if the message could not be saved
        return null;
    }

    public List<Message> getAllMessages() {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            // SQL query to retrieve all messages
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Iterate over the result set and create Message objects
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return the list of messages
        return messages; 
    }

    public Message getMessageById(int messageId) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;

        try {
            // SQL query to retrieve a message by its ID
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();

            // If the message exists, create a Message object
            if (rs.next()) {
                message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return the message by ID
        return message; 
    }

    public Message deleteMessageById(int messageId) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;

        try {
            // Fetch the message from the database to return it in the response
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
            // If the message exists, delete it
            if (message != null) {
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
                deleteStmt.setInt(1, messageId);
                deleteStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return the deleted message or null if no message was found
        return message;
    }

    public Message updateMessageById(Message message) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;

        try {
            // Check if the message exists
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getMessage_id());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // If the message exists, update its message_text
                String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setString(1, message.getMessage_text());
                updateStmt.setInt(2, message.getMessage_id());
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    // Retrieve the updated message from the database
                    String selectUpdatedSql = "SELECT * FROM message WHERE message_id = ?";
                    PreparedStatement selectUpdatedStmt = connection.prepareStatement(selectUpdatedSql);
                    selectUpdatedStmt.setInt(1, message.getMessage_id());
                    ResultSet updatedRs = selectUpdatedStmt.executeQuery();

                    if (updatedRs.next()) {
                        // Construct the updated message object
                        updatedMessage = new Message(
                                updatedRs.getInt("message_id"),
                                updatedRs.getInt("posted_by"),
                                updatedRs.getString("message_text"),
                                updatedRs.getLong("time_posted_epoch")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return the updated message or null if not found or failed
        return updatedMessage;
    }

    public List<Message> getAllMessagesByAccountId(Account account) {
        // Establish a database connection
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            // SQL query to select all messages by the given account ID
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account.getAccount_id());  
            ResultSet rs = ps.executeQuery();

            // Iterate through the result set and create Message objects
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return the list of messages
        return messages;  
    }
}

