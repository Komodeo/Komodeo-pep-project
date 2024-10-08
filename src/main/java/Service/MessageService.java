package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    // map MessageDAO
    MessageDAO messageDAO;

    // No-args constructor for an messageService instantiates a plain MessageDAO
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a messageService when a messageDAO is provided.
     * This is used for when a mock messageDAO that exhibits mock behavior is used
     * in the test cases.
     * This would allow the testing of MessageService independently of MessageDAO.
     * 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * DONE: Use the messageDAO to persist a message to the database.
     * 
     * @param message a message object.
     * @return message if it was successfully persisted, null if it was not
     *         successfully persisted (eg if the message_text is blank.)
     */
    public Message addMessage(Message message) {
        if (message.getMessage_text() == "") {
            return null;
        }
        return messageDAO.insertMessage(message);
    }

    /**
     * DONE: Use the messageDAO to retrieve all messages.
     * 
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /*
     * DONE: Use the messageDAO to retrieve a message using its ID.
     * 
     * @param message_id the id of the message to retrieve
     * @return message a message object
     */
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    /*
     * DONE: Use the messageDAO to delete a message using its ID.
     * 
     * @param message_id the id of the message to delete
     * @return message a message object
     */
    public Message deleteMessageById(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    /*
     * DONE: Use the messageDAO to update a message text using its ID.
     * 
     * @param message_id the id of the message to update
     * @param message_text the updated text for the message
     * @return message a message object
     */
    public Message updateMessageTextById(int message_id, String message_text) {
        return messageDAO.updateMessageTextById(message_id, message_text);
    }

    /*
     * DONE: Use the messageDAO to retrieve all messages from a specific user.
     * 
     * @param account_id the id of the user
     * @return messages all messages by user
     */
    public List<Message> getAllMessagesByUser(int account_id) {
        return messageDAO.getAllMessagesByUser(account_id);
    }

}
