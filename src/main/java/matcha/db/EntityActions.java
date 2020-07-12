package matcha.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.converter.Utils;
import matcha.mail.Sender;
import matcha.model.ImageElem;
import matcha.model.Profile;
import matcha.model.User;
import matcha.model.UserAndProfile;
import matcha.response.ResponseError;
import matcha.response.ResponseOk;
import matcha.response.ResponseOkData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class EntityActions {

    @Autowired
    EntityManipulator entityManipulator;

    @Autowired
    Sender mailSender;

    public Object userRegistry(User user) {
        Optional<Integer> userExist = entityManipulator.getUserCountByLogin(user.getLogin());
        if (userExist.isEmpty() || userExist.get() != 0) {
            StringBuilder sb = new StringBuilder()
                    .append("userRegistry. User exist: ");
            log.info(sb.toString().concat(user.toString()));
            sb.append(user.getLogin());
            return new ResponseError("error", sb.toString());
        }

        Optional<Integer> emptyProfile = entityManipulator.createEmptyProfile();
        if (emptyProfile.isEmpty()) {
            StringBuilder sb = new StringBuilder()
                    .append("userRegistry. Error create user profile: ");
            log.error(sb.toString().concat(user.toString()));
            sb.append(user.getLogin());
            return new ResponseError("error", sb.toString());
        }
        user.setProfileId(emptyProfile.get());

        Optional<Integer> userCreated = entityManipulator.createUser(user);
        if (userCreated.isEmpty() || userCreated.get() != 1) {
            StringBuilder sb = new StringBuilder()
                    .append("userRegistry. Error create user: ");
            log.error(sb.toString().concat(user.toString()));
            sb.append(user.getLogin());
            return new ResponseError("error", sb.toString());
        }
        boolean b = mailSender.sendRegistrationMail(user.getEmail(), user.getActivationCode());
        if (!b) {
            Optional<Integer> userCountByLogin = entityManipulator.getUserCountByLogin(user.getLogin());
            if (userCountByLogin.isPresent() && userCountByLogin.get() == 1) {
                Optional<Integer> integer = entityManipulator.dropUserByLogin(user.getLogin());
                if (integer.isEmpty() || integer.get() != 1)
                    return new ResponseError("error", "NO NAME ERROR!");
            }
        }
        return new ResponseOk("ok", "CREATED", user.getLogin());
    }

//    public Object userUpdate(User user) {
//        Optional<Integer> userExist = entityManipulator.getUserCountByLogin(user.getLogin());
//        if (userExist.isEmpty() || userExist.get() != 0) {
//            StringBuilder sb = new StringBuilder()
//                    .append("userRegistry. User exist: ");
//            log.info(sb.toString().concat(user.toString()));
//            sb.append(user.getLogin());
//            return new ResponseError("error", sb.toString());
//        }
//        Optional<Integer> userCreated = entityManipulator.createUser(user);
//        if (userCreated.isEmpty() || userCreated.get() != 1) {
//            StringBuilder sb = new StringBuilder()
//                    .append("userRegistry. Error create user: ");
//            log.error(sb.toString().concat(user.toString()));
//            sb.append(user.getLogin());
//            return new ResponseError("error", sb.toString());
//        }
//        boolean b = mailSender.sendRegistrationMail(user.getEmail(), user.getActivationCode());
//        if (!b) {
//            Optional<Integer> userCountByLogin = entityManipulator.getUserCountByLogin(user.getLogin());
//            if (userCountByLogin.isPresent() && userCountByLogin.get() == 1) {
//                Optional<Integer> integer = entityManipulator.dropUserByLogin(user.getLogin());
//                if (integer.isEmpty() || integer.get() != 1)
//                    return new ResponseError("error", "NO NAME ERROR!");
//            }
//        }
//        return new ResponseOk("ok", "CREATED", user.getLogin());
//    }

    public boolean getVerificationToken(String uuid) {
        Optional<Integer> userCountByActivationCode = entityManipulator.getUserCountByActivationCode(uuid);
        if (userCountByActivationCode.isEmpty() || userCountByActivationCode.get() != 1)
            return false;
        Optional<User> userByActivationCode = entityManipulator.getUserByActivationCode(uuid);
        if (userByActivationCode.isEmpty())
            return false;
        userByActivationCode.get().setActivationCode(null);
        userByActivationCode.get().setActive(true);
        Optional<Integer> integer = entityManipulator.updateUserById(userByActivationCode.get());
        return integer.isPresent() && integer.get() == 1;
    }

    public Object userLogin(String login, String password, Object location) {
        String message = "";
        Optional<User> userByLogin = null;
        try {
            userByLogin = entityManipulator.getUserByLogin(login);
        } catch (Exception ignored) {
        }
        if (userByLogin != null && userByLogin.isPresent()) {
            User user = userByLogin.get();
            if (user.isActive() && !user.isBlocked()) {
                if (Utils.checkPassword(password, user.getSalt(), user.getPassword())) {
                    user.setActivationCode(UUID.randomUUID().toString());
                    user.setTime(Calendar.getInstance().getTime());
                    //сохранить location в базу
                    Optional<Integer> integer = entityManipulator.updateUserById(user);
                    if (integer.isPresent() && integer.get() == 1) {
                        log.info("userLogin. User ".concat(user.getLogin()).concat(" logged in successfully"));
                        return new ResponseOk("ok", user.getActivationCode(), user.getLogin());
                    } else
                        message = "Login failed";
                } else
                    message = "Username or password is incorrect";
            } else
                message = "User ".concat(login).concat(" is blocked or inactive");
        } else
            message = "User ".concat(login).concat(" not found");
        log.info("userLogin. ".concat(message));
        return new ResponseError("error", message);
    }

    public Object userUpdate(User user) {

        Optional<User> userByActivationCode = entityManipulator.getUserByActivationCode(user.getActivationCode());

        if (userByActivationCode.isEmpty()) {
            log.warn("User with activationCode '{}' not found!", user.getActivationCode());
            return new ResponseError("error", "Перелогинься!");
        }
        user.setProfileId(userByActivationCode.get().getProfileId());

        Optional<Integer> userCountByLoginAndActivationCode =
                entityManipulator.getUserCountByLoginAndActivationCode(user.getLogin(), user.getActivationCode());
        if (userCountByLoginAndActivationCode.isPresent() && userCountByLoginAndActivationCode.get() == 0) {
            Optional<User> userByLogin = entityManipulator.getUserByLogin(user.getLogin());
            if (userByLogin.isPresent()) {
                log.warn("User with login '{}' exist!", user.getActivationCode());
                return new ResponseError("error", "Поьзователь с логином " + user.getLogin() + " уже существует!");
            }
        }

        return entityManipulator.updateUserByActivationCode(user);
    }

    public Object profileSave(Profile profile) {

        Optional<Profile> profileById = entityManipulator.getProfileById(profile.getId());
        if (profileById.isEmpty()) {
            log.error("profileSave. Error load profile images '{}'", profile);
            return new ResponseError("error", "Ошибка! Не удалось сохранить профиль!");
        }

        if (profileById.get().getImagesIds() != null)
        profileById.get().getImagesIds().forEach(imageId -> {
            Optional<Integer> dropRes = entityManipulator.dropImageById(imageId);
            if (dropRes.isEmpty() || dropRes.get() != 1) {
                log.error("profileSave. Error delete image [id = {}]", imageId);
            }
        });

        if (profile.getImages() != null)
        profile.getImages().forEach(imageElem -> {
            Optional<Integer> integer = entityManipulator.insertImage(imageElem);
            if (integer.isEmpty()) {
                log.error("profileSave. Error to save image [{}]", imageElem);
            }
            else {
                imageElem.setId(integer.get());
            }
        });

        if (profile.getImages() != null) {
            ImageElem imageElem1 = profile.getImages().stream()
                    .filter(imageElem -> imageElem.getIndex() == profile.getAvatar())
                    .findFirst()
                    .orElse(null);
            if (imageElem1 != null) {
                profile.setAvatar(imageElem1.getId());
            }
        }

        Optional<Integer> profileUpdate = entityManipulator.updateProfileById(profile);
        if (profileUpdate.isEmpty() || profileUpdate.get() != 1) {
            log.error("profileSave. Error update profile [{}]", profile);
            return new ResponseError("error", "Ошибка! Не удалось сохранить профиль!!!");
        }

        return true;


//        User user = Converter.convertToUser(json);
//        Optional<User> userByLogin = entityManipulator.getUserByLogin(user.getLogin());
//        String message = "";

//        entityManipulator.updateProfileById(profile);

//                    if (integer.isPresent() && integer.get() == 1) {
//                        //все ок
//                        return true;
//                    } else {
//                        //откат (удаление профиля)
//                        Optional<Integer> integer1 = entityManipulator.dropProfileById(profile1.get());
//                        if (integer1.isPresent() && integer1.get() == 1) {
//                            //откат успешен
//                            message = "User profile create failed. Error update user.";
//                        } else
//                            message = "User profile create failed.";
//                    }



//        return new ResponseError("error", message);
    }

    public Object profileGet(User user) {

        Optional<User> userByActivationCode = entityManipulator.getUserByActivationCode(user.getActivationCode());

        if (userByActivationCode.isEmpty()) {
            log.warn("profileGet. User with activationCode '{}' not found!", user.getActivationCode());
            return new ResponseError("error", "Пользователь не найден!");
        }

        Optional<Profile> profileById = entityManipulator.getProfileById(userByActivationCode.get().getProfileId());
        if (profileById.isEmpty()) {
            log.error("profileGet. Error load profile for user '{}'", userByActivationCode.get());
            return new ResponseError("error", "Ошибка! Не удалось загрузить профиль!");
        }

        UserAndProfile userAndProfile = new UserAndProfile(userByActivationCode.get(), profileById.get());

        return new ResponseOkData("ok", userAndProfile.toString());
    }
}
