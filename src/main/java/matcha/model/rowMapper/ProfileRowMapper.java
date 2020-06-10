package matcha.model.rowMapper;

import matcha.model.Profile;
import matcha.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

public class ProfileRowMapper implements RowMapper<Profile> {
    @Override
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile profile = new Profile();
        profile.setId(rs.getInt("id"));
        profile.setAge(rs.getInt("age"));
        profile.setAvatar(rs.getInt("avatar"));
        profile.setBiography(rs.getString("biography"));
        profile.setGender(rs.getInt("gender"));
        profile.setPreference(rs.getInt("preference"));
        profile.setImages(Arrays.asList(rs.getString("images").split(",")));
        profile.setTags(Arrays.asList(rs.getString("tags").split(",")));
        return profile;
    }
}