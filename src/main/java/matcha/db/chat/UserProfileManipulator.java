package matcha.db.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Select;
import matcha.model.Profile;
import matcha.model.rowMapper.ProfileRowMapper;
import matcha.model.rowMapper.UserRowMapper;
import matcha.user.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileManipulator {

    private final JdbcTemplate jdbcTemplate;

    public User getUserByLogin(String login) {
        log.info("Get user by login [{}]", login);
        User user = jdbcTemplate.queryForObject(Select.selectUserByLogin, new UserRowMapper(), login);
        log.info("Get user by login result: {}", user);
        return user;
    }

    public Profile getProfileById(int profileId) {
        log.info("Get profile by id: {}", profileId);
        Profile profile = jdbcTemplate.queryForObject(Select.selectProfileById, new ProfileRowMapper(), profileId);
        log.info("Get profile by id: {} Profile: {}", profileId, profile);
        return profile;
    }
}
