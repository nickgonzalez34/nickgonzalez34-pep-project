package Service;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.ArrayList;
import java.util.List;

import DAO.AccountDAO;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public Message createMessage(Message message) {
        // Validate the message text is not null or blank
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            throw new IllegalArgumentException("Must enter a message.");  
        }
        // Validate the text does not exceed 255 characters
        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text cannot be more than 255 characters.");  
        }

        // Validate that the user exists
        if (accountDAO.findByAccountId(message.getPosted_by()) == null) {
            throw new IllegalArgumentException("Message text cannot be more than 255 characters."); 
        }

        // If validation passes, save the message to the database
        return messageDAO.saveMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId); 
    }

    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId); 
    }

    public Message updateMessageById(int messageId, String newMessageText) {
        // Validate the message text is not null or blank
        if (newMessageText == null || newMessageText.isBlank()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }

        // Validate that the new message text does not exceed 255 characters
        if (newMessageText.length() > 255) {
            throw new IllegalArgumentException("Message text cannot be more than 255 characters.");
        }

        // Validate that the messageId already exists
        if (messageDAO.getMessageById(messageId) == null) {
            throw new IllegalArgumentException("Message ID does not exist.");
        }

        // Create a message object with the new message text to update in the database
        Message messageToUpdate = new Message();
        messageToUpdate.setMessage_id(messageId);
        messageToUpdate.setMessage_text(newMessageText);

        // Update the message in the database
        return messageDAO.updateMessageById(messageToUpdate);
    }

    public List<Message> getAllMessagesByAccountId(Account account) {
        // Validate if the account Id exists, if not return empty list
        if (account == null || accountDAO.findByAccountId(account.getAccount_id()) == null) {
            List<Message> emptyList = new ArrayList<>();
            return emptyList;
        }
        // Retrieve the messages using the Account object
        List<Message> messages = messageDAO.getAllMessagesByAccountId(account);

        // If no messages found, return an empty list 
        if (messages == null) {
            messages = new ArrayList<>();
        }
        // Return all messages by Account Id
        return messages;
    }
}
