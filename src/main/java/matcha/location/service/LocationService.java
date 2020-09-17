package matcha.location.service;

import lombok.AllArgsConstructor;
import matcha.location.manipulation.LocationManipulator;
import matcha.mail.MailService;
import matcha.model.Location;
import matcha.properties.ConfigProperties;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocationService {

    private ValidationMessageService validationMessageService;
    private LocationManipulator locationManipulator;
    private ConfigProperties configProperties;
    private final MailService mailService;

    public Location getLocationByLogin(int userId) {
        return locationManipulator.getLocationByUserIdAndActive(userId);
    }
}
