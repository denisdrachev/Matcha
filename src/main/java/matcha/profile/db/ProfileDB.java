package matcha.profile.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.exception.db.*;
import matcha.model.rowMapper.ProfileRowMapper;
import matcha.profile.model.ProfileModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileDB {

    private final JdbcTemplate jdbcTemplate;

    public Integer insertEmptyProfile() {
        log.info("Create empty profile");
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int update = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(Insert.insertProfile, new String[]{"id"});
                    ps.setNull(1, Types.INTEGER);
                    ps.setNull(2, Types.INTEGER);
                    ps.setNull(3, Types.VARCHAR);
                    ps.setNull(4, Types.VARCHAR);
                    ps.setNull(5, Types.VARCHAR);
                    ps.setNull(6, Types.VARCHAR);
                    ps.setNull(7, Types.VARCHAR);
                    return ps;
                }
            }, keyHolder);
            log.info("Create empty profile result: {}", keyHolder.getKey());
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            log.warn("Exception. insertEmptyProfile: {}", e.getMessage());
            throw new InsertEmptyProfileDBException();
        }
    }

    public Integer getProfileCountById(int profileId) {
        log.info("Get profile cound by ID. profileId: {}", profileId);
        try {
            Integer integer = jdbcTemplate.queryForObject(Select.selectProfilesCountById,
                    Integer.class, profileId);
            log.info("Get profile cound by ID. profileId: {}", profileId);
            return integer;
        } catch (Exception e) {
            log.warn("Exception. getProfileCountById: {}", e.getMessage());
            throw new GetProfileCountByIdDBException();
        }
    }

    public ProfileModel getProfileById(int profileId) {
        log.info("Get profile by ID. profileId: {}", profileId);
        try {
            ProfileModel profile = jdbcTemplate.queryForObject(Select.selectProfileById,
                    new ProfileRowMapper(), profileId);
            log.info("Get profile by id: ".concat(String.valueOf(profileId)).concat(" Profile: ") + profile);
            return profile;
        } catch (Exception e) {
            log.warn("Exception. getProfileById: {}", e.getMessage());
            throw new GetProfileByIdDBException();
        }
    }

    //TODO вынести лишний функционал
    public void updateProfileById(ProfileModel profile, String preference, String imagesIds) {
        log.info("Update profile by ID. profile: {}", profile);
        try {
            int update = jdbcTemplate.update(Update.updateProfileById,
                    profile.getAge(), profile.getGender(), preference, profile.getBiography(),
                    String.join(",", profile.getTags()), imagesIds, profile.getAvatar(), profile.getId());
            log.info("Update profile result: {}", update);
        } catch (Exception e) {
            log.warn("Exception. updateProfileById: {}", e.getMessage());
            throw new UpdateProfileByIdDBException();
        }
    }

    public Integer dropProfileById(int id) {
        log.info("Drop profile by id. id: {}", id);
        try {
            int drop = jdbcTemplate.update(Drop.deleteProfileById, id);
            log.info("Drop profile by id result: {}", drop);
            return drop;
        } catch (Exception e) {
            log.warn("Exception. dropProfileById: {}", e.getMessage());
            throw new DropProfileByIdDBException();
        }
    }
}
