package matcha.user.service;

import lombok.AllArgsConstructor;
import matcha.converter.Utils;
import matcha.location.service.LocationService;
import matcha.mail.MailService;
import matcha.model.Location;
import matcha.properties.ConfigProperties;
import matcha.response.Response;
import matcha.user.manipulation.UserManipulator;
import matcha.user.model.UserEntity;
import matcha.user.model.UserInfo;
import matcha.user.model.UserRegistry;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserInterface {

    private ValidationMessageService validationMessageService;
    private UserManipulator userManipulator;
    private LocationService locationService;
    private ConfigProperties configProperties;
    private final MailService mailService;

    public Response userRegistration(UserRegistry userRegistry) {
        UserEntity userEntity = new UserEntity(userRegistry);
        Utils.initRegistryUser(userEntity, userEntity.getPassword());
        userEntity.setActive(configProperties.isUsersDefaultActive());
        userManipulator.userRegistry(userEntity);
        //TODO если не удалось отправить сообщение - удалять пользователя
        mailService.sendRegistrationMail(userEntity.getEmail(), userEntity.getActivationCode());
        return validationMessageService.prepareMessageOkOnlyType();
    }

    public Response userLogin(UserInfo user) {
        return userManipulator.userLogin(user);
    }


    public void checkUserToToken(String token) {
        userManipulator.checkUserActivationCode(token);
//        Object o = validateOnlyBySchema(schemaName, json);
//        if (o instanceof Boolean) {
//            try {
//                User user = Converter.convertToUser(json);
//                final ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
//                Utils.initRegistryUser(user, node.get("password").asText());
//                user.setActive(configProperties.isUsersDefaultActive());
//                o = user;
//            } catch (Exception e) {
//                log.error("Error. Error mapping json: " + json);
//            }
//        }
//        return o;
    }

    public UserEntity getUserByLogin(String login) {
        UserEntity userByLogin = userManipulator.getUserByLogin(login);
        Location locationByLogin = locationService.getLocationByLogin(userByLogin.getId());
        userByLogin.setLocation(locationByLogin);
        return userByLogin;
    }

    public UserEntity getUserByToken(String token) {
        return userManipulator.getUserByToken(token);
    }

    public void saveUser(UserEntity user) {
        userManipulator.userUpdate(user);
    }
}
