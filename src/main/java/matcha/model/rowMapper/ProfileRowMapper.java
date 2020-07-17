package matcha.model.rowMapper;

import matcha.converter.Converter;
import matcha.model.Profile;
import matcha.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProfileRowMapper implements RowMapper<Profile> {
    @Override
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile profile = new Profile();
        profile.setId(rs.getInt("id"));
        profile.setAge(rs.getInt("age") == 0 ? null : rs.getInt("age"));
        profile.setAvatar(rs.getInt("avatar") == 0 ? null : rs.getInt("avatar"));
        profile.setBiography(rs.getString("biography"));
        profile.setGender(rs.getInt("gender") == 0 ? null : rs.getInt("gender"));
        if (rs.getString("preference") != null)
            profile.setPreference(Stream.of(rs.getString("preference").split(","))
                    .map(Integer::parseInt).collect(Collectors.toList()));

        if (rs.getString("images") != null && !rs.getString("images").isEmpty()) {
            profile.setImagesIds(Stream.of(rs.getString("images").split(",")).collect(Collectors.toList()));
        }

        if (rs.getString("tags") != null)
            profile.setTags(Arrays.asList(rs.getString("tags").split(",")));
        System.err.println(profile);
        return profile;
    }
}