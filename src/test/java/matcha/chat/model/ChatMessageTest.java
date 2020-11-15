package matcha.chat.model;


import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

class ChatMessageTest {

    @Test
    void resultTest() {
        List<ChatMessage> chatMessages = List.of(
                new ChatMessage(1, "user1", "user2", "message1", new Date(), true),
                new ChatMessage(1, "user1", "user2", "message2", new Date(), true),
                new ChatMessage(1, "user1", "user2", "message3", new Date(), true),
                new ChatMessage(1, "user1", "user2", "message4", new Date(), true)
        );
//        ChatMessage message = new ChatMessage(1, "user1", "user2", "message1", new Date(), true);
        System.err.println(chatMessages);

//        chatMessages.stream().forEach(System.out::println);
    }

}