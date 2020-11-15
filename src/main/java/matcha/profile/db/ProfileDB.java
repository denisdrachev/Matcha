package matcha.profile.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Drop;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.exception.context.UserRegistrationException;
import matcha.exception.db.DropProfileByIdDBException;
import matcha.exception.db.GetProfileByIdDBException;
import matcha.exception.db.GetProfileCountByIdDBException;
import matcha.exception.db.UpdateProfileByIdDBException;
import matcha.model.rowMapper.ProfileRowMapper;
import matcha.profile.model.ProfileEntity;
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
                    ps.setBoolean(6, false);
                    return ps;
                }
            }, keyHolder);
            log.info("Create empty profile result: {}", keyHolder.getKey());
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            log.warn("Exception. insertEmptyProfile: {}", e.getMessage());
            throw new UserRegistrationException();
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

    public ProfileEntity getProfileById(int profileId) {
        log.info("Get profile by ID: {}", profileId);
        try {
            ProfileEntity profile = jdbcTemplate.queryForObject(Select.selectProfileById,
                    new ProfileRowMapper(), profileId);
            log.info("Get profile by ID:{} result:{}", profileId, profile);
            return profile;
        } catch (Exception e) {
            log.warn("Exception. getProfileById: {}", e.getMessage());
            throw new GetProfileByIdDBException("Ошибка. Не удалось загрузить профиль");
        }
    }

    public void updateProfileById(ProfileEntity profile) {
        log.info("Update profile by ID. profile: {}", profile);
        try {
            int update = jdbcTemplate.update(Update.updateProfileById,
                    profile.getAge(), profile.getGender(), profile.getPreferenceAsString(),
                    profile.getBiography(), profile.getTagsAsString(), profile.getId());
            log.info("Update profile result: {}", update);
        } catch (Exception e) {
            log.warn("Exception. updateProfileById: {}", e.getMessage());
            throw new UpdateProfileByIdDBException();
        }
    }

    public void dropProfileById(int id) {
        log.info("Drop profile by id. id: {}", id);
        try {
            int drop = jdbcTemplate.update(Drop.deleteProfileById, id);
            log.info("Drop profile by id result: {}", drop);
        } catch (Exception e) {
            log.warn("Exception. dropProfileById: {}", e.getMessage());
            throw new DropProfileByIdDBException();
        }
    }
}
