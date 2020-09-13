package matcha.db.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.chat.model.ChatMessage;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.model.rowMapper.ChatMessageRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO можно логирование переделать под AOP
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatEntityManipulator {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Integer> insertChatMessage(ChatMessage message) {
        log.info("Insert chat message '{}'", message);
        if (message == null) return Optional.empty();
        int insert = jdbcTemplate.update(Insert.insertChatMessage,
                message.getToLogin(), message.getFromLogin(), message.getMessage(), message.getTime(), false);
        log.info("Insert chat message result: {}", insert);
        return Optional.of(insert);
    }

    public Optional<Integer> updateChatMessage(ChatMessage message) {
        log.info("Update chat message '{}'", message);
        if (message == null) return Optional.empty();
        int update = jdbcTemplate.update(Update.updateChatMessage,
                true, message.getId());
        log.info("Update chat message result: {}", update);
        return Optional.of(update);
    }

    public List<ChatMessage> getChatMessages(String toLogin, String fromLogin, int limit) {
        log.info("Get chat messages toLogin: {} fromLogin: {} limit: {}", toLogin, fromLogin, limit);
        try {
            List<ChatMessage> result = jdbcTemplate.query(Select.selectChatMessages, new ChatMessageRowMapper(),
                    toLogin, fromLogin, limit);
            log.info("Get chat messages. Count: {}", result.size());
            return result;
        } catch (Exception e) {
            log.warn("Failed to load chat messages");
            return List.of();
        }
    }

    public List<ChatMessage> getFullChatMessages(String toLogin, String fromLogin, int limit) {
        log.info("Get full chat messages toLogin: {} fromLogin: {} limit: {}", toLogin, fromLogin, limit);
        try {
            List<ChatMessage> result = jdbcTemplate.query(Select.selectFullChatMessages, new ChatMessageRowMapper(),
                    toLogin, fromLogin, toLogin, fromLogin, limit);
            log.info("Get full chat messages. Count: {}", result.size());
            return result;
        } catch (Exception e) {
            log.warn("Failed to load full chat messages");
            return List.of();
        }
    }

    public List<ChatMessage> getNewChatMessages(String toLogin, String fromLogin) {
        log.info("Get new chat messages toLogin: {} fromLogin: {}", toLogin, fromLogin);
        try {
            List<ChatMessage> result = jdbcTemplate.query(Select.selectNewChatMessages, new ChatMessageRowMapper(),
                    toLogin, fromLogin);
            log.info("Get new chat messages. Count: {}", result.size());
            return result;
        } catch (Exception e) {
            log.warn("Failed to load new chat messages");
            return List.of();
        }
    }

    public List<ChatMessage> getAllNewChatMessages(String toLogin) {
        log.info("Get all new chat messages toLogin: {}", toLogin);
        try {
            List<ChatMessage> result = jdbcTemplate.query(Select.selectCountAllNewChatMessages, new ChatMessageRowMapper(),
                    toLogin);
            log.info("Get all new chat messages. Count: {}", result.size());
            return result;
        } catch (Exception e) {
            log.warn("Failed to load all new chat messages");
            return List.of();
        }
    }
}
