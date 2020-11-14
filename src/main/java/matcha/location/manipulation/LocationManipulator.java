package matcha.location.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.exception.db.location.GetActiveLocationByLoginException;
import matcha.location.db.LocationDB;
import matcha.location.model.Location;
import matcha.mail.MailService;
import matcha.profile.db.ProfileDB;
import matcha.user.db.UserDB;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationManipulator {

    private final UserDB userDB;
    private final ProfileDB profileDB;
    private final LocationDB locationDB;

    private final MailService mailService;
    private final ValidationMessageService validationMessageService;

    public Location getLocationByUserIdAndActive(Integer userId) {
        try {
            return locationDB.getLocationByUserIdAndActive(userId);
        } catch (GetActiveLocationByLoginException e) {
            return new Location();
        }
    }

    public void insertLocation(Location location) {
        locationDB.insertLocation(location);
    }

    public Integer updateLocation(Location location) {
        return locationDB.updateLocation(location);
    }

    public void deactivationLocationByLogin(String login) {
        locationDB.updateActiveLocationByLogin(false, login);
    }

    public List<Location> getAllLocations() {
        return locationDB.getLocations();
    }
}
