package matcha.chat.component;

import matcha.response.Response;

public interface ChatInterface {

    Response saveMessage(String toLogin, String fromLogin, String message);

    Response getMessages(String toLogin, String fromLogin, int limit);

    Response getFullMessages(String toLogin, String fromLogin, int limit);

    Response getNewMessages(String toLogin, String fromLogin);
}
