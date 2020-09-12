package matcha.chat.component;

import lombok.AllArgsConstructor;
import matcha.chat.model.ChatMessage;
import matcha.db.chat.ChatEntityManipulator;
import matcha.response.Response;
import matcha.response.ResponseDataList;
import matcha.response.ResponseError;
import matcha.response.ResponseOnlyType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ChatComponent implements ChatInterface {

    ChatEntityManipulator chatEntityManipulator;

    @Override
    public Response saveMessage(String toLogin, String fromLogin, String message) {
        ChatMessage chatMessage = new ChatMessage(toLogin, fromLogin, message);
        Optional<Integer> result = chatEntityManipulator.insertChatMessage(chatMessage);
        return result.isPresent()
                ? new ResponseOnlyType("success")
                : new ResponseError("error", "Не удалось отправить сообщение");
    }

    @Override
    public Response getMessages(String toLogin, String fromLogin, int limit) {
        List<ChatMessage> chatMessages = chatEntityManipulator.getChatMessages(toLogin, fromLogin, limit);
        chatMessages.stream()
                .filter(chatMessage -> !chatMessage.isRead())
                .forEach(chatEntityManipulator::updateChatMessage);
        return new ResponseDataList("success", chatMessages);
    }

    @Override
    public Response getFullMessages(String toLogin, String fromLogin, int limit) {
        List<ChatMessage> chatMessages = chatEntityManipulator.getFullChatMessages(toLogin, fromLogin, limit);
        chatMessages.stream()
                .filter(chatMessage -> !chatMessage.isRead())
                .forEach(chatEntityManipulator::updateChatMessage);
        return new ResponseDataList("success", chatMessages);
    }

    @Override
    public Response getNewMessages(String toLogin, String fromLogin) {
        List<ChatMessage> chatMessages = chatEntityManipulator.getNewChatMessages(toLogin, fromLogin);
        chatMessages.forEach(chatEntityManipulator::updateChatMessage);
        return new ResponseDataList("success", chatMessages);
    }
}
