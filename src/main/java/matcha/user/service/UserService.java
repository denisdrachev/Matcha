package matcha.user.service;

import lombok.AllArgsConstructor;
import matcha.converter.Utils;
import matcha.event.model.Event;
import matcha.event.service.EventService;
import matcha.location.model.Location;
import matcha.location.service.LocationService;
import matcha.mail.MailService;
import matcha.profile.model.ProfileEntity;
import matcha.profile.model.UserProfileWithoutEmail;
import matcha.profile.service.ProfileService;
import matcha.properties.ConfigProperties;
import matcha.response.Response;
import matcha.user.manipulation.UserManipulator;
import matcha.user.model.UserEntity;
import matcha.user.model.UserInfo;
import matcha.user.model.UserRegistry;
import matcha.user.model.UserUpdateEntity;
import matcha.userprofile.model.UserInfoModel;
import matcha.utils.EventType;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserInterface {

    private UserManipulator userManipulator;
    private LocationService locationService;
    private ConfigProperties configProperties;
    private final MailService mailService;
    private final ProfileService profileService;
    private EventService eventService;

    public void userRegistration(UserRegistry userRegistry) {

        userManipulator.checkUserExistByLogin(userRegistry.getLogin());

        Integer newProfileId = profileService.createNewProfile();

        UserEntity userEntity = new UserEntity(userRegistry);
        userEntity.setActive(configProperties.isUsersDefaultActive());
        userEntity.setProfileId(newProfileId);
        Utils.initRegistryUser(userEntity);
        userManipulator.userRegistry(userEntity);

        mailService.sendRegistrationMail(userEntity.getEmail(), userEntity.getActivationCode());

        Event newEvent = new Event(EventType.REGISTRATION, userRegistry.getLogin(), false, "");
        eventService.saveEvent(newEvent);
        userRegistry.getLocation().setProfileId(newProfileId);
        locationService.saveLocation(userRegistry.getLocation());
    }

    public Response userLogin(UserInfo user) {
        Response response = userManipulator.userLogin(user);
        Event newEvent = new Event(EventType.LOGIN, user.getLogin(), false, "");
        eventService.saveEvent(newEvent);
        return response;
    }

    public void checkUserToToken(String token) {
        userManipulator.checkUserByToken(token);
    }

    public UserEntity getUserByLogin(String login) {
        UserEntity userByLogin = userManipulator.getUserByLogin(login);
        Location locationByLogin = locationService.getLocationByUserId(userByLogin.getId());
        userByLogin.setLocation(locationByLogin);
        return userByLogin;
    }

    public UserEntity getUserByToken(String token) {
        return userManipulator.getUserByToken(token);
    }

    public void saveUser(UserUpdateEntity user) {
        locationService.deactivationLocationByLogin(user.getLogin());
        userManipulator.userUpdate(user);
    }

    public boolean activationUserByToken(String token) {
        return userManipulator.activationUserByToken(token);
    }

    public UserProfileWithoutEmail getUserProfile(String login) {
        UserEntity user = getUserByLogin(login);
        Location activeUserLocation = locationService.getLocationByUserId(user.getId());
        user.setLocation(activeUserLocation);
        ProfileEntity profileById = profileService.getProfileByIdWithImages(user.getProfileId());

        Event newEvent = new Event(EventType.PROFILE_LOAD, login, false, "");
        eventService.saveEvent(newEvent);

        return new UserProfileWithoutEmail(user, profileById);
    }

    //TODO рефакторинг
    public void saveUserInfo(UserInfoModel userInfo) {

        UserEntity currentUser = getUserByLogin(userInfo.getLogin());

        saveUser(new UserUpdateEntity(userInfo));

        userInfo.getLocation().setProfileId(currentUser.getId());
        userInfo.getLocation().setActive(true);
        locationService.saveLocation(userInfo.getLocation());

//        Integer userProfileId = userManipulator.getUserProfileId(userInfo.getLogin());
        ProfileEntity newProfile = new ProfileEntity(currentUser.getProfileId(), userInfo);

        profileService.updateProfile(currentUser.getProfileId(), newProfile);

        Event newEvent = new Event(EventType.PROFILE_UPDATE, userInfo.getLogin(), false, "");
        eventService.saveEvent(newEvent);
    }

    public void checkUserByLoginAndActivationCode(String login, String token) {
        userManipulator.checkUserByLoginAndActivationCode(login, token);
    }
}
