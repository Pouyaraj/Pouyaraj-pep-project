package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import java.util.Map;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

     public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
     }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler); 

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);

            Account newAccount = accountService.registerAccount(account.getUsername(), account.getPassword());

            if (newAccount != null) {

                context.json(newAccount);
            } else {

                context.status(400);
            }
        } catch (Exception e) {

            context.status(400);
        }
    }

    private void postLoginHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());

            if (loggedInAccount != null) {
                context.json(loggedInAccount); 
            } else {
                context.status(401);
            }
        } catch (Exception e) {
            context.status(401);
        }
    }

    private void postMessageHandler(Context context){
        try {
            Message message = context.bodyAsClass(Message.class);
    
            Message newMessage = messageService.createMessage(message);
    
            if (newMessage != null) {
                context.json(newMessage);
            } else {
                context.status(400);
            }
        } catch (Exception e) {
            context.status(400);
        }
    }

    private void getAllMessageHandler(Context context){
        context.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context context){
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            context.json(message);
        } else {
            context.json("");
        }
    }

    private void deleteMessageHandler(Context context){
        try {

            String messageIdStr = context.pathParam("message_id");
            int messageId = Integer.parseInt(messageIdStr);
            Message messageToDelete = messageService.getMessageById(messageId);

            if (messageToDelete != null) {
                messageService.deleteMessage(messageId);
                context.json(messageToDelete);
            } else {
                
                context.status(200);
            }
        } catch (Exception e) {
            context.status(500);
        }
    }
    
    private void patchMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
    
        Map<String, Object> body = context.bodyAsClass(Map.class);
    
        String newMessageText = (String) body.get("message_text");
    
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            context.status(400);
            return;
        }
    
        Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);
    
        if (updatedMessage != null) {
            context.json(updatedMessage);
            context.status(200);
        } else {
            context.status(400);
        }
    }
    

    private void getMessagesByAccountHandler(Context context) {
            String accountIdStr = context.pathParam("account_id");
            int accountId = Integer.parseInt(accountIdStr);
            List<Message> messages = messageService.getMessagesByAccountId(accountId);

            context.json(messages);
    }
}