package matcha.user.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.exception.db.DropUserByLoginDBException;
import matcha.exception.db.GetUserCountByLoginDBException;
import matcha.exception.db.GetUserProfileIdByLoginDBException;
import matcha.exception.db.UpdateUserByIdDBException;
import matcha.exception.db.location.GetLocationsException;
import matcha.exception.service.UserRegistryException;
import matcha.exception.user.UserAuthException;
import matcha.exception.user.UserLoginException;
import matcha.exception.user.UserNotFoundException;
import matcha.model.rowMapper.UserRowMapper;
import matcha.user.model.UserEntity;
import matcha.user.model.UserUpdateEntity;
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
import java.util.List;

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
            throw new UserRegistryException();
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
            log.info("Exception. getUserByToken: {}", e.getMessage());
            //мб другое искючение тут?
            throw new UserAuthException();
        }
    }

    public void checkUserByToken(String token) {
        log.info("Check user by Activation Code. activationCode: {}", token);
        try {
            Integer count = jdbcTemplate.queryForObject(Select.selectUsersCountByActivationCode, Integer.class, token);
            log.info("Check user by Activation Code. Result user: {}", count);
        } catch (Exception e) {
            log.info("Exception. checkUserByToken: {}", e.getMessage());
            throw new UserAuthException();
        }
    }

    public Integer checkUserByLoginAndToken(String login, String activationCode) {
        log.info("Get user count by login and activation code. login:{} activationCode:{}", login, activationCode);
        try {
            Integer usersCount = jdbcTemplate.queryForObject(Select.selectUsersCountByLoginAndActivationCode, Integer.class, login, activationCode);
            log.info("Get user count by login and activation code result: {}", usersCount);
            return usersCount;
        } catch (Exception e) {
            log.info("Exception. checkUserByLoginAndToken: {}", e.getMessage());
            throw new UserNotFoundException();
        }
    }

    public void updateUserByLogin(UserUpdateEntity user) {
        log.info("Update user by login: {}", user);
        try {
            int update = jdbcTemplate.update(Update.updateUserByLogin,
                    user.getFname(), user.getLname(), user.getEmail(), user.getTime(), user.getLogin());
            log.info("Update user by login result: {}", update);
        } catch (Exception e) {
            log.warn("Exception. updateUserByActivationCode: {}", e.getMessage());
            throw new UserLoginException();
        }
    }

    public void updateUserByLogin(UserEntity user) {
        log.info("Update user by login: {}", user);
        try {
            int update = jdbcTemplate.update(Update.updateUserByLogin,
                    user.getLogin(), user.getFname(), user.getLname(), user.getEmail(), user.getTime());
            log.info("Update user by login result: {}", update);
        } catch (Exception e) {
            log.warn("Exception. updateUserByActivationCode: {}", e.getMessage());
            throw new UserLoginException();
        }
    }

    public Integer getUserProfileIdByLogin(String login) {
        log.info("Get profile id by user login: {}", login);
        try {
            Integer profileId = jdbcTemplate.queryForObject(Select.selectUserProfileIdByLogin, Integer.class, login);
            log.info("Get profile id by user login:{} result:{}", login, profileId);
            return profileId;
        } catch (Exception e) {
            log.warn("Exception. getUserProfileIdByLogin: {}", e.getMessage());
            throw new GetUserProfileIdByLoginDBException();
        }
    }

    public List<UserEntity> getAllUsers() {
        log.info("Get all users");
        try {
            List<UserEntity> query = jdbcTemplate.query(Select.selectUsers, new UserRowMapper());
            log.info("Get all users result count: {}", query.size());
            return query;
        } catch (Exception e) {
            log.warn("Exception. getLocations: {}", e.getMessage());
            throw new GetLocationsException();
        }
    }
}
