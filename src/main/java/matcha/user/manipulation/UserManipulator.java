package matcha.user.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.converter.Utils;
import matcha.exception.context.UserAlreadyExistException;
import matcha.exception.db.*;
import matcha.exception.db.location.GetActiveLocationByLoginException;
import matcha.exception.db.location.InsertLocationException;
import matcha.exception.service.UserRegistryException;
import matcha.exception.user.UserBlockedOrDisabledException;
import matcha.exception.user.UserLoginException;
import matcha.exception.user.UserLoginOrPasswordIncorrectException;
import matcha.location.db.LocationDB;
import matcha.location.manipulation.LocationManipulator;
import matcha.mail.MailService;
import matcha.model.Location;
import matcha.profile.db.ProfileDB;
import matcha.response.Response;
import matcha.response.ResponseOk;
import matcha.user.db.UserDB;
import matcha.user.model.UserEntity;
import matcha.user.model.UserLogin;
import matcha.validator.ValidationService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManipulator {

    private final UserDB userDB;
    private final LocationDB locationDB;
    private final LocationManipulator locationManipulator;
    private final ProfileDB profileDB;
    private final MailService mailService;
    private final ValidationService validationService;

    public void userRegistry(UserEntity user) {
        try {
            Integer userWithLoginCount = userDB.getUserCountByLogin(user.getLogin());
            if (userWithLoginCount != 0) {
                log.info("User with login {} already exist.", user.getLogin());
                throw new UserAlreadyExistException("Пользователь с логином " + user.getLogin() + " уже существует.");
            }

            Integer emptyProfile = profileDB.insertEmptyProfile();
            user.setProfileId(emptyProfile);

            int userCreated = userDB.insertUser(user);
            user.getLocation().setUser(userCreated);
            locationDB.insertLocation(user.getLocation());

            mailService.sendRegistrationMail(user.getEmail(), user.getActivationCode());
        } catch (GetUserCountByLoginDBException | InsertEmptyProfileDBException | InsertUserDBException q) {
            throw new UserRegistryException();
        } catch (InsertLocationException | SendRegistrationMailException ile) {
            try {
                userDB.dropUserByLogin(user.getLogin());
            } catch (Exception e) {
                throw new UserRegistryException();
            }
        }
    }

    public Response userLogin(UserLogin userLogin) {
        try {
            UserEntity user = userDB.getUserByLogin(userLogin.getLogin());
            userLogin.getLocation().setUser(user.getId());
            locationDB.insertLocation(userLogin.getLocation());

            if (user.isActive() && !user.isBlocked()) {
                if (Utils.checkPassword(userLogin.getPassword(), user.getSalt(), user.getPasswordBytes())) {
                    user.setActivationCode(UUID.randomUUID().toString());
                    user.setTime(Calendar.getInstance().getTime());
                    //сохранить location в базу

                    userDB.updateUserById(user);
                    //TODO рефакторинг ответа
                    return new ResponseOk("ok", user.getActivationCode(), user.getLogin());
                } else {
                    log.info("Логин или пароль неверны. User: {}", userLogin);
                    throw new UserLoginOrPasswordIncorrectException();
                }
            } else {
                String format = String.format("Пользователь %s заблокирован или неактивен", userLogin.getLogin());
                log.info(format);
                throw new UserBlockedOrDisabledException();
            }
        } catch (InsertLocationException | UpdateUserByIdDBException ile) {
            log.info("Ошибка авторизации пользователя {}.", userLogin.getLogin());
            throw new UserLoginException();
        }
    }


    //TODO подумать, мб переделать, чтобы изолировать от получения пользователя из бд
    public void userUpdate(UserEntity user) {

        UserEntity userByActivationCode = userDB.getUserByActivationCode(user.getActivationCode());

        user.setProfileId(userByActivationCode.getProfileId());
        user.getLocation().setUser(userByActivationCode.getId());
        user.setActive(userByActivationCode.isActive());
        user.setBlocked(userByActivationCode.isBlocked());

        if (user.getLocation().isActive()) {
            try {
                Location locationByLogin = locationManipulator.getActiveLocationByLogin(userByActivationCode.getId());
                locationByLogin.setActive(false);
                locationDB.updateLocation(locationByLogin);
            } catch (GetActiveLocationByLoginException e) {
            }
            user.getLocation().setActive(true);
        }
        locationDB.insertLocation(user.getLocation());
        userDB.getUserCountByLoginAndActivationCode(user.getLogin(), user.getActivationCode());
        userDB.updateUserByActivationCode(user);
    }

    public UserEntity getUserByLogin(String login) {
        return userDB.getUserByLogin(login);
    }

}
