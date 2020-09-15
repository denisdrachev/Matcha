package matcha.location.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.location.db.LocationDB;
import matcha.mail.MailService;
import matcha.model.Location;
import matcha.profile.db.ProfileDB;
import matcha.user.db.UserDB;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationManipulator {

    private final UserDB userDB;
    private final ProfileDB profileDB;
    private final LocationDB locationDB;

    private final MailService mailService;
    private final ValidationMessageService validationMessageService;

    public Location getActiveLocationByLogin(Integer userId) {
        return locationDB.getActiveLocationByLogin(userId);
    }

}
