package matcha.location.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.crud.Insert;
import matcha.db.crud.Select;
import matcha.db.crud.Update;
import matcha.exception.db.location.GetActiveLocationByLoginException;
import matcha.exception.db.location.GetLocationsException;
import matcha.exception.db.location.InsertLocationException;
import matcha.exception.db.location.UpdateLocationException;
import matcha.model.Location;
import matcha.model.rowMapper.LocationRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationDB {

    private final JdbcTemplate jdbcTemplate;

    public List<Location> getLocations() {
        log.info("Get all locations...");
        try {
            List<Location> query = jdbcTemplate.query(Select.selectLocations, new LocationRowMapper());
            log.info("Get all locations result count: {}", query.size());
            return query;
        } catch (Exception e) {
            log.warn("Exception. getLocations: {}", e.getMessage());
            throw new GetLocationsException();
        }
    }

    public void insertLocation(Location location) {
        log.info("Insert location '{}...'", location);
        try {
            int insert = jdbcTemplate.update(Insert.insertLocation, location.getUser(),
                    location.getX(), location.getY(), location.getTime(), location.isActive());
            log.info("Insert image result: {}", insert);
        } catch (Exception e) {
            log.warn("Exception. insertLocation: {}", e.getMessage());
            throw new InsertLocationException();
        }
    }

    public Location getActiveLocationByLogin(Integer userId) {
        log.info("Get active location by login {}...", userId);
        try {
            Location location = jdbcTemplate.queryForObject(Select.selectLocationByLoginAndActive,
                    new LocationRowMapper(), userId);
            log.info("Get active location by login done. Result: {}", location);
            return location;
        } catch (Exception e) {
            log.error("Exception. getActiveLocationByLogin: {}", e.getMessage());
            throw new GetActiveLocationByLoginException();
        }
    }

    public Integer updateLocation(Location location) {
        try {
            log.info("Update location {}...", location);
            int update = jdbcTemplate.update(Update.updateLocationById, location.isActive(), location.getId());
            log.info("Update location done. Result: {}", update);
            return update;
        } catch (Exception e) {
            log.warn("Exception. updateLocation: {}", e.getMessage());
            throw new UpdateLocationException();
        }
    }
}
