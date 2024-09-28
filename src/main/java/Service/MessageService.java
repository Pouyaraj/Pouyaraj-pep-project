package Service;
import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        if (message == null) {
            return null;
        }

        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()) {
            return null;
        }

        if (message.getMessage_text().length() > 255) {
            return null;
        }

        if (!AccountDAO.userExists(message.getPosted_by())) {
            return null;
        }

        // Call the DAO method to create the message
        return messageDAO.createMessage(message);
    }
}
