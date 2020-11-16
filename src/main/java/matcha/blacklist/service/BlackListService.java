package matcha.blacklist.service;

import lombok.AllArgsConstructor;
import matcha.blacklist.manipulation.BlackListManipulator;
import matcha.blacklist.model.BlackListMessage;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlackListService {

    private BlackListManipulator blackListManipulator;

    public BlackListMessage getBlackListMessage(String fromLogin, String toLogin) {
        return blackListManipulator.getBlackListMessage(fromLogin, toLogin);
    }

    public boolean isBlackListExists(String fromLogin, String toLogin) {
        return blackListManipulator.isBlackListExists(fromLogin, toLogin);
    }

    public void updateBlackListMessage(BlackListMessage message) {
        blackListManipulator.updateBlackListMessage(message);
    }

    public void insertBlackListMessage(BlackListMessage message) {
        blackListManipulator.insertBlackListMessage(message);
    }
}
