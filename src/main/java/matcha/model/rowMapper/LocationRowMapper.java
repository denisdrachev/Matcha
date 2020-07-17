package matcha.model.rowMapper;

import matcha.model.Location;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationRowMapper implements RowMapper<Location> {

    @Override
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        Location location = new Location();
        location.setId(rs.getInt("id"));
        location.setUser(rs.getInt("user"));
        location.setX(rs.getDouble("x"));
        location.setY(rs.getDouble("y"));
        location.setTime(rs.getTimestamp("time"));
        return location;
    }
}