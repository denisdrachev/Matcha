package matcha.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.mail.Sender;
import matcha.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class EntityActions {

    @Autowired
    EntityManipulator entityManipulator;

    @Autowired
    Sender mailSender;

    public String userRegistry(User user) {
        Optional<Integer> userExist = entityManipulator.getUserCountByLogin(user.getLogin());
        if (userExist.isEmpty() || userExist.get() != 0) {
            StringBuilder sb = new StringBuilder()
                    .append("userRegistry. User exist: ")
                    .append(user.toString());
            log.info(sb.toString());
            return sb.toString();
        }
        Optional<Integer> userCreated = entityManipulator.createUser(user);
        if (userCreated.isEmpty() || userCreated.get() != 1) {
            StringBuilder sb = new StringBuilder()
                    .append("userRegistry. Error create user: ")
                    .append(user.toString());
            log.error(sb.toString());
            return sb.toString();
        }
        boolean b = mailSender.sendRegistrationMail(user.getEmail(), user.getActivationCode());
        if (!b) {
            Optional<Integer> userCountByLogin = entityManipulator.getUserCountByLogin(user.getLogin());
            if (userCountByLogin.isPresent() && userCountByLogin.get() == 1) {
                Optional<Integer> integer = entityManipulator.dropUserByLogin(user.getLogin());
                if (integer.isEmpty() || integer.get() != 1)
                    return "ERROR";
            }
        }
        return "OK";
    }

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
}
