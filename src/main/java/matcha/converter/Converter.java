package matcha.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import matcha.model.OnlyAction;
import matcha.model.Profile;
import matcha.model.User;
import matcha.response.ResponseError;

import java.util.Optional;

@Slf4j
public class Converter {

    public static OnlyAction convertToOnlyAction(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OnlyAction object = null;
        try {
            object = mapper.readValue(json, OnlyAction.class);
        } catch (JsonProcessingException e) {
            log.error("Error convertToOnlyAction. ".concat(e.getMessage()));
        }
        return object;
    }

    public static User convertToUser(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User object = null;
        try {
            object = mapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            log.error("Error convertToUser. ".concat(e.getMessage()));
        }
        return object;
    }

    public static Profile convertToProfile(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Profile object = null;
        try {
            object = mapper.readValue(json, Profile.class);
        } catch (JsonProcessingException e) {
            log.error("Error convertToProfile. ".concat(e.getMessage()));
        }
        return object;
    }

    public static String convertStringToResponseErrorJson(String s) {
        ResponseError error = new ResponseError("error", s);
        return error.toString();
    }

    @SneakyThrows
    public static Optional<String> objectToJson(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        return Optional.ofNullable(objectMapper.writeValueAsString(o));
    }
}
