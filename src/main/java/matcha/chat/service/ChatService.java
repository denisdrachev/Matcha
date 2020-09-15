package matcha.chat.service;

import lombok.AllArgsConstructor;
import matcha.chat.manipulation.ChatManipulator;
import matcha.chat.model.ChatMessage;
import matcha.response.Response;
import matcha.userprofile.model.UserProfileChat;
import matcha.userprofile.service.UserProfileService;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService implements ChatInterface {

    private ChatManipulator chatManipulator;
    private UserProfileService userProfileService;
    private ValidationMessageService validationMessageService;

    @Override
    public Response saveMessage(String toLogin, String fromLogin, String message) {
        ChatMessage chatMessage = new ChatMessage(toLogin, fromLogin, message);
        chatManipulator.insertChatMessage(chatMessage);
        return validationMessageService.prepareMessageOkOnlyType();
    }

    //мб и не нужен этот функционал
    @Override
    public Response getMessages(String toLogin, String fromLogin, int limit) {
        List<ChatMessage> chatMessages = chatManipulator.getChatMessages(toLogin, fromLogin, limit);
        chatMessages.stream()
                .filter(chatMessage -> !chatMessage.isRead())
                .forEach(chatManipulator::updateChatMessage);
        return validationMessageService.prepareMessageOkDataList(chatMessages);
    }

    @Override
    public Response getFullMessages(String toLogin, String fromLogin, int limit) {
        List<ChatMessage> chatMessages = chatManipulator.getFullChatMessages(toLogin, fromLogin, limit);
        chatMessages.stream()
                .filter(chatMessage -> !chatMessage.isRead())
                .forEach(chatManipulator::updateChatMessage);
        return validationMessageService.prepareMessageOkDataList(chatMessages);
    }

    @Override
    public Response getNewMessages(String toLogin, String fromLogin, int isRead) {
        List<ChatMessage> chatMessages = chatManipulator.getNewChatMessages(toLogin, fromLogin);
        if (isRead != 0)
            chatMessages.forEach(chatManipulator::updateChatMessage);
        return validationMessageService.prepareMessageOkDataList(chatMessages);
    }

    @Override
    public Response getAllNewMessages(String toLogin) {
        List<ChatMessage> chatMessages = chatManipulator.getAllNewChatMessages(toLogin);
        List<UserProfileChat> collect = chatMessages.stream().map(chatMessage -> {
            UserProfileChat chatUserProfile = userProfileService.getChatUserProfile(chatMessage.getFromLogin());
            chatUserProfile.setChatMessages(chatMessage);
            return chatUserProfile;
        }).collect(Collectors.toList());
        return validationMessageService.prepareMessageOkDataList(collect);
    }
}
