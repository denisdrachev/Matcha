package matcha.blacklist.service;

import lombok.AllArgsConstructor;
import matcha.blacklist.manipulation.BlackListManipulator;
import matcha.blacklist.model.BlackListMessage;
import matcha.event.model.Event;
import matcha.event.service.EventService;
import matcha.response.Response;
import matcha.user.model.UserEntity;
import matcha.user.service.UserService;
import matcha.utils.EventType;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlackListService {

    private UserService userService;
    private ValidationMessageService validationMessageService;
    private EventService eventService;
    private BlackListManipulator blackListManipulator;

    public Response saveBlackList(String token, BlackListMessage message) {

        userService.checkUserToToken(token);
        UserEntity userByToken = userService.getUserByToken(token);
        userService.getUserByLogin(message.getToLogin());

        message.setFromLogin(userByToken.getLogin());

        if (blackListManipulator.isBlackListExists(message.getFromLogin(), message.getToLogin())) {
            blackListManipulator.updateBlackListMessage(message);
        } else {
            blackListManipulator.insertBlackListMessage(message);
        }

        String eventType;
        if (message.isBlocked()) {
            eventType = EventType.USER_BLOCK;
        } else {
            eventType = EventType.USER_UNBLOCK;
        }
        Event newEvent = new Event(eventType, message.getFromLogin(), true, message.getToLogin());
        eventService.saveEvent(newEvent);

        return validationMessageService.prepareMessageOkOnlyType();
    }

    public BlackListMessage getBlackListMessage(String fromLogin, String toLogin) {
        return blackListManipulator.getBlackListMessage(fromLogin, toLogin);
    }
}
