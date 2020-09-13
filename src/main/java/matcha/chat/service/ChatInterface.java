package matcha.chat.service;

import matcha.response.Response;

public interface ChatInterface {

    Response saveMessage(String toLogin, String fromLogin, String message);

    Response getMessages(String toLogin, String fromLogin, int limit);

    Response getFullMessages(String toLogin, String fromLogin, int limit);

    Response getNewMessages(String toLogin, String fromLogin, int isRead);

    Response getAllNewMessages(String toLogin);
}
