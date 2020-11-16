package matcha.blacklist.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.blacklist.model.BlackListMessage;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.exception.db.InsertBlackListMessageDBException;
import matcha.exception.db.NotFoundBlackListMessageDBException;
import matcha.exception.db.UpdateBlackListMessageDBException;
import matcha.model.rowMapper.BlackListMessageRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlackListDB {

    private final JdbcTemplate jdbcTemplate;

    public void insertBlackListMessage(BlackListMessage message) {
        log.info("Insert BlackList message '{}'", message);
        try {
            int insert = jdbcTemplate.update(Insert.insertBlacklist,
                    message.getFromLogin(), message.getToLogin(), message.isBlocked());
            log.info("Insert BlackList message result: {}", insert);
        } catch (Exception e) {
            log.warn("Exception. insertBlackListMessage: {}", e.getMessage());
            throw new InsertBlackListMessageDBException();
        }
    }

    public void updateBlackListMessage(BlackListMessage message) {
        log.info("Update BlackList message '{}'", message);
        try {
            int update = jdbcTemplate.update(Update.updateBlacklistById,
                    message.isBlocked(), message.getFromLogin(), message.getToLogin());
            log.info("Update BlackList message result: {}", update);
        } catch (Exception e) {
            log.warn("Exception. updateBlackListMessage: {}", e.getMessage());
            throw new UpdateBlackListMessageDBException();
        }
    }

    public BlackListMessage getBlackListMessage(String fromLogin, String toLogin) {
        log.info("Get BlackList message: [fromLogin:{}][toLogin:{}]", fromLogin, toLogin);
        try {
            BlackListMessage result = jdbcTemplate.queryForObject(Select.selectBlacklist,
                    new BlackListMessageRowMapper(), fromLogin, toLogin);
            log.info("Get BlackList message result: {}", result);
            return result;
        } catch (Exception e) {
            log.warn("Failed to load BlackList message. Exception message: {}", e.getMessage());
            throw new NotFoundBlackListMessageDBException();
        }
    }

    public Boolean isBlackListExists(String fromLogin, String toLogin) {
        log.info("Get BlackList message: [fromLogin:{}][toLogin:{}]", fromLogin, toLogin);
        try {
            Integer integer = jdbcTemplate.queryForObject(Select.selectBlackListCount,
                    Integer.class, fromLogin, toLogin);
            log.info("Get BlackList message result: {}", integer);
            return integer > 0;
        } catch (Exception e) {
            log.warn("Exception. isBlackListExists: {}", e.getMessage());
            return false;
        }
    }
}
