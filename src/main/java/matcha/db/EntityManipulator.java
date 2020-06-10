package matcha.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.model.Image;
import matcha.model.Profile;
import matcha.model.User;
import matcha.model.rowMapper.ProfileRowMapper;
import matcha.model.rowMapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Optional;

//TODO можно логирование переделать под AOP
@Slf4j
@Service
@AllArgsConstructor
public class EntityManipulator {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Optional<Integer> getUserCountByLogin(String login) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(Select.selectUsersCountByLogin,
                Integer.class, login));
    }

    public Optional<Integer> getUserCountByActivationCode(String activationCode) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(Select.selectUsersCountByActivationCode,
                Integer.class, activationCode));
    }

    public Optional<User> getUserByActivationCode(String activationCode) {
        User user = jdbcTemplate.queryForObject(Select.selectUserByActivationCode,
                new Object[]{activationCode}, new UserRowMapper());
        log.info("Get user by Activation Code: ".concat(activationCode).concat(" User: ") + user);
        Optional<User> userOptional = Optional.ofNullable(user);
        if (userOptional.isPresent())
            user.setTime(Calendar.getInstance().getTime());
        return userOptional;
    }

    public Optional<User> getUserById(int userId) {
        User user = jdbcTemplate.queryForObject(Select.selectUserById, User.class, userId);
        log.info("Get user by id: ".concat(String.valueOf(userId)).concat(" User: ") + user);
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByLogin(String login) {
        User user = jdbcTemplate.queryForObject(Select.selectUserByLogin,
                new UserRowMapper(), login);
        log.info("Get user by login: ".concat(login).concat(" User: ") + user);
        return Optional.ofNullable(user);
    }

    public Optional<Integer> createUser(User user) {
        log.info("Create user: ".concat(user.toString()));
        int update = jdbcTemplate.update(Insert.insertUser,
                user.getLogin(), user.getPassword(), user.getActivationCode(), user.getFname(),
                user.getLname(), user.getEmail(), user.isActive(), user.isBlocked(), user.getTime(), user.getSalt(), null);
        log.info("Create user result: ".concat(String.valueOf(update)));
        return Optional.of(update);
    }

    public Optional<Integer> updateUserById(User user) {
        log.info("Update user: ".concat(user.toString()));
        int update = jdbcTemplate.update(Update.updateUserById,
                user.getLogin(), user.getActivationCode(),
                user.getFname(), user.getLname(), user.getEmail(),
                user.isActive(), user.isBlocked(), user.getTime(), user.getProfileId(), user.getId());
        log.info("Update user result: ".concat(String.valueOf(update)));
        return Optional.of(update);
    }

    public Optional<Integer> dropUserById(String id) {
        log.info("Drop user by id: ".concat(id));
        int drop = jdbcTemplate.update(Drop.deleteUserById, id);
        log.info("Drop user by login result: ".concat(String.valueOf(drop)));
        return Optional.of(drop);
    }

    public Optional<Integer> dropUserByLogin(String login) {
        log.info("Drop user by login: ".concat(login));
        int drop = jdbcTemplate.update(Drop.deleteUserById, login);
        log.info("Drop user by login result: ".concat(String.valueOf(drop)));
        return Optional.of(drop);
    }

    /**************************************************************************************************************/

    public Optional<Integer> getProfileCountById(int profileId) {
        Integer integer = jdbcTemplate.queryForObject(Select.selectProfilesCountById,
                Integer.class, profileId);
        return Optional.ofNullable(integer);
    }

    public Optional<Profile> getProfileById(int profileId) {
        Profile profile = jdbcTemplate.queryForObject(Select.selectProfileById,
                new ProfileRowMapper(), profileId);
        log.info("Get profile by id: ".concat(String.valueOf(profileId)).concat(" Profile: ") + profile);
        return Optional.ofNullable(profile);
    }

    public Optional<Integer> createProfile(Profile profile) {
        log.info("Create profile: ".concat(profile.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(Insert.insertProfile, new String[]{"id"});
                ps.setInt(1, profile.getAge());
                ps.setInt(2, profile.getGender());
                ps.setInt(3, profile.getPreference());
                ps.setString(4, profile.getBiography());
                ps.setString(5, String.join(",", profile.getTags()));
                ps.setString(6, String.join(",", profile.getImages()));
                ps.setInt(7, profile.getAvatar());
                return ps;
            }
        }, keyHolder);
        log.info("Create profile result: ".concat(String.valueOf(keyHolder.getKey())));
        if (update != 1)
            return Optional.empty();
        return Optional.of(keyHolder.getKey().intValue());
    }

    public Optional<Integer> updateProfileById(Profile profile) {
        log.info("Update profile: ".concat(profile.toString()));
        int update = jdbcTemplate.update(Update.updateProfileById,
                profile.getAge(), profile.getGender(), profile.getPreference(), profile.getBiography(),
                String.join(",", profile.getTags()), String.join(",", profile.getImages()), profile.getAvatar(), profile.getId());
        log.info("Update profile result: ".concat(String.valueOf(update)));
        return Optional.of(update);
    }

    public Optional<Integer> dropProfileById(int id) {
        log.info("Drop user by id: ".concat(String.valueOf(id)));
        int drop = jdbcTemplate.update(Drop.deleteProfileById, id);
        log.info("Drop user by login result: ".concat(String.valueOf(drop)));
        return Optional.of(drop);
    }

    /**************************************************************************************************************/

    public Optional<Integer> getImageCountById(int imageId) {
        Integer integer = jdbcTemplate.queryForObject(Select.selectImagesCountById,
                Integer.class, imageId);
        return Optional.ofNullable(integer);
    }

    public Optional<Image> getImageById(int imageId) {
        Image image = jdbcTemplate.queryForObject(Select.selectImageById, Image.class, imageId);
        log.info("Get image by id: ".concat(String.valueOf(imageId)).concat(" Image: ") + image);
        return Optional.ofNullable(image);
    }

    public Optional<Integer> createImage(Image image) {
        log.info("Create image: ".concat(image.toString()));
        int update = jdbcTemplate.update(Insert.insertImage, image.getImg());
        log.info("Create image result: ".concat(String.valueOf(update)));
        return Optional.of(update);
    }

    public Optional<Integer> updateImageById(Image image) {
        log.info("Update image: ".concat(image.toString()));
        int update = jdbcTemplate.update(Update.updateImageById, image.getImg(), image.getId());
        log.info("Update image result: ".concat(String.valueOf(update)));
        return Optional.of(update);
    }


}
