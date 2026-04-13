package WebChat.WebChat.service;

import WebChat.WebChat.enity.Message;

public interface MessageService {
    Message savePrivateMessage(Message msg);
    Message saveGroupMessage(Message msg);
}
