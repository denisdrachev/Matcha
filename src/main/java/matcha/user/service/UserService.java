package matcha.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserInterface {

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
