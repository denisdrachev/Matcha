package matcha.user.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.exception.db.DropUserByLoginDBException;
import matcha.exception.db.GetUserCountByLoginDBException;
import matcha.exception.db.InsertUserDBException;
import matcha.exception.db.UpdateUserByIdDBException;
import matcha.exception.user.UserAuthException;
import matcha.exception.user.UserLoginException;
import matcha.exception.user.UserNotFoundException;
import matcha.model.rowMapper.UserRowMapper;
import matcha.user.model.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDB {

    private final JdbcTemplate jdbcTemplate;

    public Integer getUserCountByLogin(String login) {
        log.info("Get user count by login. Login: {}", login);
        try {
            Integer resultCount = jdbcTemplate.queryForObject(Select.selectUsersCountByLogin, Integer.class, login);
            log.info("Get user count by login. Login: {}", login);
            return resultCount;
        } catch (Exception e) {
            log.warn("Exception. getUserCountByLogin: {}", e.getMessage());
            throw new GetUserCountByLoginDBException();
        }
    }

    public UserEntity getUserByLogin(String login) {
        log.info("Get user by login [{}]", login);
        try {
            UserEntity user = jdbcTemplate.queryForObject(Select.selectUserByLogin, new UserRowMapper(), login);
            log.info("Get user by login result: {}", user);
            return user;
        } catch (Exception e) {
            log.warn("Exception. getUserByLogin: {}", e.getMessage());
            throw new UserNotFoundException(login);
        }
    }

    public int insertUser(UserEntity user) {
        log.info("Insert user: {}", user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            int userId = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(Insert.insertUser, new String[]{"id"});
                    ps.setString(1, user.getLogin());
                    ps.setBytes(2, user.getPasswordBytes());
                    ps.setString(3, user.getActivationCode());
                    ps.setString(4, user.getFname());
                    ps.setString(5, user.getLname());
                    ps.setString(6, user.getEmail());
                    ps.setBoolean(7, user.isActive());
                    ps.setBoolean(8, user.isBlocked());
                    ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                    ps.setBytes(10, user.getSalt());
                    ps.setInt(11, user.getProfileId());
                    return ps;
                }
            }, keyHolder);
            log.info("Insert user result userId: {}", userId);
            return userId;
        } catch (Exception e) {
            log.warn("Exception. insertUser: {}", e.getMessage());
            throw new InsertUserDBException();
        }
    }

    public void updateUserById(UserEntity user) {
        log.info("Update user: {}", user);
        try {
            int update = jdbcTemplate.update(Update.updateUserById,
                    user.getLogin(), user.getActivationCode(),
                    user.getFname(), user.getLname(), user.getEmail(),
                    user.isActive(), user.isBlocked(), user.getTime(), user.getProfileId(), user.getId());
            log.info("Update user result: ".concat(String.valueOf(update)));
        } catch (Exception e) {
            log.warn("Exception. updateUserById: {}", e.getMessage());
            throw new UpdateUserByIdDBException();
        }
    }

    public void dropUserByLogin(String login) {
        log.info("Drop user by login: {}", login);
        try {
            int drop = jdbcTemplate.update(Drop.deleteUserById, login);
            log.info("Drop user by login result: {}", drop);
        } catch (Exception e) {
            log.warn("Exception. dropUserByLogin: {}", e.getMessage());
            throw new DropUserByLoginDBException();
        }
    }

    //TODO рефакторинг метода
    public UserEntity getUserByToken(String activationCode) {
        log.info("Get user by Activation Code. activationCode: {}", activationCode);
        try {
            UserEntity user = jdbcTemplate.queryForObject(Select.selectUserByActivationCode,
                    new Object[]{activationCode}, new UserRowMapper());
            log.info("Get user by Activation Code. Result user: {}", user);
            user.setTime(Calendar.getInstance().getTime());
            return user;
        } catch (Exception e) {
            log.info("getUserByActivationCode. User with activation code {} not found", activationCode);
            //мб другое искючение тут?
            throw new UserAuthException();
        }
    }

    public void checkUserByActivationCode(String token) {
        log.info("Check user by Activation Code. activationCode: {}", token);
        try {
            Integer count = jdbcTemplate.queryForObject(Select.selectUsersCountByActivationCode, Integer.class, token);
            log.info("Check user by Activation Code. Result user: {}", count);
        } catch (Exception e) {
            log.info("checkUserByActivationCode. User with activation code {} not found", token);
            throw new UserAuthException();
        }
    }

    public Integer getUserCountByLoginAndActivationCode(String login, String activationCode) {
        log.info("Get user count by login and activation code. login:{} activationCode:{}", login, activationCode);
        try {
            Integer usersCount = jdbcTemplate.queryForObject(Select.selectUsersCountByLoginAndActivationCode, Integer.class, login, activationCode);
            log.info("Get user count by login and activation code result: {}", usersCount);
            return usersCount;
        } catch (Exception e) {
            log.info("getUserByActivationCode. User count by login {} with activation code {} not found", login, activationCode);
            throw new UserNotFoundException();
        }
    }

    public void updateUserByActivationCode(UserEntity user) {
        log.info("Update user by activation code: {}", user);
        try {
            int update = jdbcTemplate.update(Update.updateUserByActivationCode,
                    user.getLogin(),
                    user.getFname(), user.getLname(), user.getEmail(),
                    user.isActive(), user.isBlocked(), user.getTime(), user.getActivationCode());
            log.info("Update user by activation code result: {}", update);
        } catch (Exception e) {
            throw new UserLoginException();
        }
    }
}
