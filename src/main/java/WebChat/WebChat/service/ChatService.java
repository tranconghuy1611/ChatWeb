package WebChat.WebChat.service;



import WebChat.WebChat.enity.Message;

import java.util.List;

public interface ChatService {

    Message sendPrivateMessage(String sender, String receiver, String content);

    Message sendGroupMessage(String sender, String roomId, String content);

    List<Message> getPrivateMessages(String user1, String user2);

    List<Message> getRoomMessages(String roomId);

}