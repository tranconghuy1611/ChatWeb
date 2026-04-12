package WebChat.WebChat.dto.response;

public class ChatResponse {
    private String sender;
    private String receiver;
    private String content;

    public ChatResponse(String sender, String content, String receiver  ) {
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
    }

    // getter
}