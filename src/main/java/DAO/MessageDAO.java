package DAO;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;

public class MessageDAO {
    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Specify to return generated keys
            
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generatedMessageId = pkeyResultSet.getInt(1);
                message.setMessage_id(generatedMessageId); // Set generated message_id
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
