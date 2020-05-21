package matcha.model.rowMapper;

import matcha.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setFname(rs.getString("fname"));
        user.setLname(rs.getString("lname"));
        user.setActive(rs.getBoolean("active"));
        user.setActivationCode(rs.getString("activationCode"));
        user.setBlocked(rs.getBoolean("blocked"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setProfileId(rs.getInt("profileId"));
        return user;
    }
}