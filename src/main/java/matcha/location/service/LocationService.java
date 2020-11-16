package matcha.location.service;

import lombok.AllArgsConstructor;
import matcha.location.manipulation.LocationManipulator;
import matcha.location.model.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

    private LocationManipulator locationManipulator;

    public Location getLocationByUserId(int userId) {
        return locationManipulator.getLocationByUserIdAndActive(userId);
    }

    public List<Location> getAllLocations() {
        return locationManipulator.getAllLocations();
    }

    public void deactivationLocationByLogin(String login) {
        locationManipulator.deactivationLocationByLogin(login);
    }

    public void saveLocation(Location location) {
        locationManipulator.insertLocation(location);
    }
}
