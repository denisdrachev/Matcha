package matcha.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.converter.Utils;
import matcha.mail.Sender;
import matcha.model.User;
import matcha.response.ResponseError;
import matcha.response.ResponseOk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

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
        } catch (Exception ignored) {}
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
}
