package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;

import Model.Message;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    // map account service
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        return app;
    }

    /**
     * Handler to register a new user.
     * ## 1: Our API should be able to process new User registrations.
     * 
     * As a user, I should be able to create a new Account on the endpoint POST
     * localhost:8080/register. The body will contain a representation of a JSON
     * Account, but will not contain an account_id.
     * 
     * - The registration will be successful if and only if the username is not
     * blank, the password is at least 4 characters long, and an Account with that
     * username does not already exist. If all these conditions are met, the
     * response body should contain a JSON of the Account, including its account_id.
     * The response status should be 200 OK, which is the default. The new account
     * should be persisted to the database.
     * - If the registration is not successful, the response status should be 400.
     * (Client error)
     * 
     * @param ctx The Javalin Context object manages information about both the
     *            HTTP request and response.
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount == null || addedAccount.getUsername() == "" || addedAccount.getPassword().length() < 4) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    /**
     * Handler to login a user.
     * ## 2: Our API should be able to process User logins.
     * As a user, I should be able to verify my login on the endpoint POST
     * localhost:8080/login. The request body will contain a JSON representation of
     * an Account, not containing an account_id. In the future, this action may
     * generate a Session token to allow the user to securely use the site. We will
     * not worry about this for now.
     * 
     * - The login will be successful if and only if the username and password
     * provided in the request body JSON match a real account existing on the
     * database. If successful, the response body should contain a JSON of the
     * account in the response body, including its account_id. The response status
     * should be 200 OK, which is the default.
     * 
     * - If the login is not successful, the response status should be 401.
     * (Unauthorized)
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedAccount = accountService.loginAccount(account);
        if (loggedAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(loggedAccount));
        }
    }

    /*
     * Handler to post a new message.
     * ## 3: Our API should be able to process the creation of new messages.
     * 
     * As a user, I should be able to submit a new post on the endpoint POST
     * localhost:8080/messages. The request body will contain a JSON representation
     * of a message, which should be persisted to the database, but will not contain
     * a message_id.
     * 
     * - The creation of the message will be successful if and only if the
     * message_text is not blank, is not over 255 characters, and posted_by refers
     * to a real, existing user. If successful, the response body should contain a
     * JSON of the message, including its message_id. The response status should be
     * 200, which is the default. The new message should be persisted to the
     * database.
     * - If the creation of the message is not successful, the response status
     * should be 400. (Client error)
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        } else {
            ctx.status(400);
        }
    }

    /*
     * Handler to retrieve all messages.
     * ## 4: Our API should be able to retrieve all messages.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET
     * localhost:8080/messages.
     * 
     * - The response body should contain a JSON representation of a list containing
     * all messages retrieved from the database. It is expected for the list to
     * simply be empty if there are no messages. The response status should always
     * be 200, which is the default.
     */
    public void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /*
     * Handler to retrieve a message by its ID.
     * ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET
     * localhost:8080/messages/{message_id}.
     * 
     * - The response body should contain a JSON representation of the message
     * identified by the message_id. It is expected for the response body to simply
     * be empty if there is no such message. The response status should always be
     * 200, which is the default.
     */
    public void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if (message == null)
            ctx.status(200);
        else
            ctx.json(message);
    }

    /*
     * Handler to delete a message by its ID.
     * ## 6: Our API should be able to delete a message identified by a message ID.
     * 
     * As a User, I should be able to submit a DELETE request on the endpoint DELETE
     * localhost:8080/messages/{message_id}.
     * 
     * - The deletion of an existing message should remove an existing message from
     * the database. If the message existed, the response body should contain the
     * now-deleted message. The response status should be 200, which is the default.
     * - If the message did not exist, the response status should be 200, but the
     * response body should be empty. This is because the DELETE verb is intended to
     * be idempotent, ie, multiple calls to the DELETE endpoint should respond with
     * the same type of response.
     */
    public void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);
        if (message == null)
            ctx.status(200);
        else
            ctx.json(message);
    }

}