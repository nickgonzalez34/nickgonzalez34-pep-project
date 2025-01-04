package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.LoginService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService = new AccountService();
    private LoginService loginService = new LoginService();
    private MessageService messageService = new MessageService();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        return app;
    }

    private void registerHandler(Context context) throws JsonProcessingException {
        // Create an ObjectMapper to parse JSON from the HTTP request body
        ObjectMapper mapper = new ObjectMapper();
    
        try {
            // Parse the request body into an Account object
            Account account = mapper.readValue(context.body(), Account.class);
    
            // Attempt to register the account via the AccountService
            Account registeredAccount = accountService.register(account);
    
            // If the account is successfully registered, return it as a JSON response with a 200 status
            if (registeredAccount != null) {
                context.json(mapper.writeValueAsString(registeredAccount));
                context.status(200);
            } 
        } catch (IllegalArgumentException e) {
            // Catch validation errors and return a 400 Bad Request
            context.status(400);
            e.printStackTrace();
        } catch (Exception e) {
            // Catch any other unexpected exceptions and return a 500 Internal Server Error
            context.status(500);
            e.printStackTrace();  
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        // Create an ObjectMapper to parse JSON from the HTTP request body
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Parse the incoming JSON request body into an Account object
            Account loginRequest = mapper.readValue(context.body(), Account.class);
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            // Authenticate using the LoginService
            Account authenticatedAccount = loginService.authenticateLogin(username, password);

            if (authenticatedAccount != null) {
                // If authentication is successful, return the authenticated account with a 200 OK status
                context.json(authenticatedAccount);
                context.status(200);
            } else {
                // Return a 401 Unauthorized if the login fails (invalid credentials)
                context.status(401);
            }
        } catch (Exception e) {
            context.status(500);
            e.printStackTrace();
        }
    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        // Create an ObjectMapper to parse JSON from the HTTP request body
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Parse the incoming JSON request body into a Message object
            Message message = mapper.readValue(context.body(), Message.class);

            // Call the MessageService to create the message
            Message createdMessage = messageService.createMessage(message);

            if (createdMessage != null) {
                // If the message was successfully created, return the message as JSON with a 200 OK status
                context.json(createdMessage);
                context.status(200);
            } 
        } catch (IllegalArgumentException e) {
            // If the message does not meet validation requirements return a 400 Bad Request status 
            context.status(400);
            e.printStackTrace();
        } catch (Exception e) {
            // Handle unexpected exceptions
            context.status(500);
            e.printStackTrace();
        }
    }

    private void getAllMessagesHandler(Context context) {
        try {
            // Call the MessageService to fetch all messages
            List<Message> messages = messageService.getAllMessages();
    
            // Return the list of messages as JSON with a 200 OK status
            context.json(messages);
            context.status(200);
        } catch (Exception e) {
            // Handle any unexpected exceptions
            context.status(500);
            e.printStackTrace();
        }
    }

    private void getMessageByIdHandler(Context context) {
        try {
            // Extract the message_id from the path parameter
            int messageId = Integer.parseInt(context.pathParam("message_id"));
    
            // Call the MessageService to get the message by ID
            Message message = messageService.getMessageById(messageId);
    
            // If the message is found, return it as a JSON response with a 200 OK status
            if (message != null) {
                context.json(message);
                context.status(200);
            } else {
                // If no message is found, return an empty response with 200 OK status
                context.status(200);  
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions and return a 500 Internal Server Error
            context.status(500);
            e.printStackTrace();
        }
    }

    private void deleteMessageByIdHandler(Context context) {
        try {
            // Extract the message_id from the path parameter
            int messageId = Integer.parseInt(context.pathParam("message_id"));
    
            // Call the MessageService to delete the message by ID
            Message deletedMessage = messageService.deleteMessageById(messageId);
    
            // If the message was deleted, return it as a JSON response with a 200 OK status
            if (deletedMessage != null) {
                context.json(deletedMessage);
                context.status(200);
            } else {
                // If the message was not found return an empty response with 200 OK status
                context.status(200);  
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions and return a 500 Internal Server Error
            context.status(500);
            e.printStackTrace();
        }
    }

    private void updateMessageByIdHandler(Context context) {
        // Create an ObjectMapper to parse JSON from the HTTP request body
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Extract the message_id from the path parameter
            int messageId = Integer.parseInt(context.pathParam("message_id"));
    
            // Get the new message_text
            String newMessageText = mapper.readTree(context.body()).get("message_text").asText();
    
            // Call the MessageService to update the message by ID
            Message updatedMessage = messageService.updateMessageById(messageId, newMessageText);
    
            if (updatedMessage != null) {
                // If the message was successfully updated, return the updated message as JSON with a 200 OK status
                context.json(updatedMessage);
                context.status(200);
            } else {
                // If the message could not be updated return a 400 Bad Request status
                context.status(400);
            }
        } catch (IllegalArgumentException e) {
            // If the message does not meet validation requirements return a 400 Bad Request status 
            context.status(400);
            e.printStackTrace();
        } catch (Exception e) {
            // Handle unexpected exceptions and return a 500 Internal Server Error
            context.status(500);
            e.printStackTrace();
        }
    }

    private void getAllMessagesByAccountIdHandler(Context context) {
        try {
            // Get the account ID from the path parameter
            int accountId = Integer.parseInt(context.pathParam("account_id"));
    
            // Retrieve the Account object using the account ID
            Account account = accountService.findByAccountId(accountId);
    
            // Call the MessageService to get the messages for this account
            List<Message> messages = messageService.getAllMessagesByAccountId(account);
    
            // Return them as JSON with a 200 OK status
            context.json(messages);  
            context.status(200);
        } catch (Exception e) {
            // Handle unexpected exceptions and return a 500 Internal Server Error
            context.status(500);
            e.printStackTrace();
        }
    }
}