package Service;
import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;

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

        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id){
        return messageDAO.getById(message_id);
    }

    public void deleteMessage(int messageId) {
        messageDAO.deleteMessage(messageId);
    }
}
