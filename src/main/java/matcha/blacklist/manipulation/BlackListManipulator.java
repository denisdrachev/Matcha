package matcha.blacklist.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.blacklist.db.BlackListDB;
import matcha.blacklist.model.BlackListMessage;
import matcha.chat.db.ChatDB;
import matcha.chat.model.ChatMessage;
import matcha.chat.model.ChatMessageSave;
import matcha.exception.db.NotFoundBlackListMessageDBException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlackListManipulator {

    private final BlackListDB blackListDB;

    public void insertBlackListMessage(BlackListMessage blackListMessage) {
        blackListDB.insertBlackListMessage(blackListMessage);
    }

    public void updateBlackListMessage(BlackListMessage blackListMessage) {
        blackListDB.updateBlackListMessage(blackListMessage);
    }

    public BlackListMessage getBlackListMessage(String fromLogin, String toLogin) {
        try {
            return blackListDB.getBlackListMessage(fromLogin, toLogin);
        } catch (NotFoundBlackListMessageDBException e) {
            return new BlackListMessage(toLogin, fromLogin, false);
        }
    }

    public boolean isBlackListExists(String fromLogin, String toLogin) {
        return blackListDB.isBlackListExists(fromLogin, toLogin);
    }
}
