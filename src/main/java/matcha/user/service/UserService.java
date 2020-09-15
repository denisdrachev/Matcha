package matcha.user.service;

import lombok.AllArgsConstructor;
import matcha.response.Response;
import matcha.user.manipulation.UserManipulator;
import matcha.user.model.UserEntity;
import matcha.user.model.UserLogin;
import matcha.user.model.UserRegistry;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserInterface {

    private ValidationMessageService validationMessageService;
    private UserManipulator userManipulator;

    public Response userRegistration(UserRegistry userRegistry) {
        userManipulator.userRegistry(new UserEntity(userRegistry));
        return validationMessageService.prepareMessageOkOnlyType();
    }

    public Response userLogin(UserLogin user) {
        return userManipulator.userLogin(user);
    }


    public void checkUserToToken() {
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
}
