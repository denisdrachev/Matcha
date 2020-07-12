package matcha.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.model.Image;
import matcha.model.ImageElem;
import matcha.model.Profile;
import matcha.model.User;
import matcha.model.rowMapper.ProfileRowMapper;
import matcha.model.rowMapper.UserRowMapper;
import matcha.properties.StringConstants;
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
import java.sql.Types;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Optional<Integer> getUserCountByLoginAndActivationCode(String login, String activationCode) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(Select.selectUsersCountByLoginAndActivationCode,
                Integer.class, login, activationCode));
    }

    public Optional<User> getUserByActivationCode(String activationCode) {
        try {
            User user = jdbcTemplate.queryForObject(Select.selectUserByActivationCode,
                    new Object[]{activationCode}, new UserRowMapper());
            log.info("Get user by Activation Code: ".concat(activationCode).concat(" User: ") + user);
            Optional<User> userOptional = Optional.ofNullable(user);
            if (userOptional.isPresent()) {
                user.setTime(Calendar.getInstance().getTime());
            }
            return userOptional;
        } catch (Exception e) {
            log.info("getUserByActivationCode. User with activation code {} not fpund", activationCode);
            return Optional.empty();
        }
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
                user.getLname(), user.getEmail(), user.isActive(), user.isBlocked(), user.getTime(),
                user.getSalt(), user.getProfileId());
        log.info("Create user result: {}", update == 1 ? "success" : "error");
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

    public Optional<Integer> updateUserByActivationCode(User user) {
        log.info("Update user: ".concat(user.toString()));
        int update = jdbcTemplate.update(Update.updateUserByActivationCode,
                user.getLogin(),
                user.getFname(), user.getLname(), user.getEmail(),
                user.isActive(), user.isBlocked(), user.getTime(), user.getActivationCode());
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

    public Optional<Integer> createEmptyProfile() {
        log.info("Create empty profile");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(Insert.insertProfile, new String[]{"id"});
                ps.setNull(1, Types.INTEGER);
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setInt(7, -1);
                return ps;
            }
        }, keyHolder);
        log.info("Create empty profile result: ".concat(String.valueOf(keyHolder.getKey())));
        if (update != 1)
            return Optional.empty();
        return Optional.of(keyHolder.getKey().intValue());
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
//TODO изменить работу с изображением
//                ps.setString(6, String.join(",", profile.getImages()));
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
        log.info("Update profile: {}", profile);

//TODO аватар доделать
        String imagesIds = profile.getImages().stream().map(imageElem -> String.valueOf(imageElem.getId())).collect(Collectors.joining(","));

        int update = jdbcTemplate.update(Update.updateProfileById,
                profile.getAge(), profile.getGender(), profile.getPreference(), profile.getBiography(),
                String.join(",", profile.getTags()), imagesIds, profile.getAvatar(), profile.getId());
        log.info("Update profile result: {}", update);
        return Optional.of(update);
    }

    public Optional<Integer> dropProfileById(int id) {
        log.info("Drop user by id: ".concat(String.valueOf(id)));
        int drop = jdbcTemplate.update(Drop.deleteProfileById, id);
        log.info("Drop user by id result: ".concat(String.valueOf(drop)));
        return Optional.of(drop);
    }

    /**************************************************************************************************************/

    public Optional<Integer> getImageCountById(int imageId) {
        Integer integer = jdbcTemplate.queryForObject(Select.selectImagesCountById,
                Integer.class, imageId);
        return Optional.ofNullable(integer);
    }

    public Optional<ImageElem> getImageById(String imageId) {
        ImageElem image = jdbcTemplate.queryForObject(Select.selectImageById, ImageElem.class, imageId);
        log.info("Get image by id: ".concat(imageId).concat(" Image: ") + image);
        return Optional.ofNullable(image);
    }

//    public Optional<Integer> createImage(Image image) {
//        log.info("Create image: ".concat(image.toString()));
//        int update = jdbcTemplate.update(Insert.insertImage, image.getImg());
//        log.info("Create image result: ".concat(String.valueOf(update)));
//        return Optional.of(update);
//    }

    public Optional<Integer> insertImage(ImageElem image) {
        log.info("Insert image '{}'", image);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(Insert.insertImage, new String[]{"id"});
                ps.setInt(1, image.getIndex());
                ps.setString(2, image.getSrc());
                return ps;
            }
        }, keyHolder);
        log.info("Insert image result: ".concat(String.valueOf(keyHolder.getKey())));
        if (update != 1)
            return Optional.empty();
        return Optional.of(keyHolder.getKey().intValue());
    }

    public Optional<Integer> updateImageById(ImageElem image) {
        log.info("Update image: ".concat(image.toString()));
        int update = jdbcTemplate.update(Update.updateImageById, image.getIndex(), image.getSrc(), image.getId());
        log.info("Update image result: ".concat(String.valueOf(update)));
        return Optional.of(update);
    }

    public Optional<Integer> dropImageById(String id) {
        log.info("Drop image by id: ".concat(id));
        int drop = jdbcTemplate.update(Drop.deleteImageById, id);
        log.info("Drop image by id result: ".concat(String.valueOf(drop)));
        return Optional.of(drop);
    }
}
