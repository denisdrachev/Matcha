package matcha.chat.service;

import lombok.AllArgsConstructor;
import matcha.chat.model.ChatMessage;
import matcha.db.chat.ChatEntityManipulator;
import matcha.response.Response;
import matcha.response.ResponseDataList;
import matcha.response.ResponseError;
import matcha.response.ResponseOnlyType;
import matcha.userprofile.model.UserProfileChat;
import matcha.userprofile.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService implements ChatInterface {

    ChatEntityManipulator chatEntityManipulator;
    UserProfileService userProfileService;

    @Override
    public Response saveMessage(String toLogin, String fromLogin, String message) {
        ChatMessage chatMessage = new ChatMessage(toLogin, fromLogin, message);
        Optional<Integer> result = chatEntityManipulator.insertChatMessage(chatMessage);
        return result.isPresent()
                ? new ResponseOnlyType("success")
                : new ResponseError("error", "Не удалось отправить сообщение");
    }

    //мб и не нужен этот функционал
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
    public Response getNewMessages(String toLogin, String fromLogin, int isRead) {
        List<ChatMessage> chatMessages = chatEntityManipulator.getNewChatMessages(toLogin, fromLogin);
        if (isRead != 0)
            chatMessages.forEach(chatEntityManipulator::updateChatMessage);
        return new ResponseDataList("success", chatMessages);
    }

    @Override
    public Response getAllNewMessages(String toLogin) {
        List<ChatMessage> chatMessages = chatEntityManipulator.getAllNewChatMessages(toLogin);
        List<UserProfileChat> collect = chatMessages.stream().map(chatMessage -> {
            UserProfileChat chatUserProfile = userProfileService.getChatUserProfile(chatMessage.getFromLogin());
            chatUserProfile.setChatMessages(chatMessage);
            return chatUserProfile;
        }).collect(Collectors.toList());
        return new ResponseDataList("success", collect);
    }
}
