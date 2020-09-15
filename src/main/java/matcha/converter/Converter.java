package matcha.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import matcha.image.model.ImageModel;
import matcha.model.OnlyAction;
import matcha.profile.model.ProfileModel;
import matcha.response.ResponseError;
import matcha.user.model.UserEntity;

import java.util.List;
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

    public static UserEntity convertToUser(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UserEntity object = null;
        try {
            object = mapper.readValue(json, UserEntity.class);
        } catch (JsonProcessingException e) {
            log.error("Error convertToUser. ".concat(e.getMessage()));
        }
        return object;
    }

    public static ProfileModel convertToProfile(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ProfileModel object = null;
        try {
            object = mapper.readValue(json, ProfileModel.class);
        } catch (JsonProcessingException e) {
            log.error("Error convertToProfile. ".concat(e.getMessage()));
        }
        return object;
    }

    public static List<ImageModel> convertToImages(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<ImageModel> object = null;
        try {
            object = mapper.readValue(json, new TypeReference<List<ImageModel>>() {
            });
//            object = mapper.readValue(json, List.class);

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
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put()
        ObjectMapper objectMapper = new ObjectMapper();
        return Optional.ofNullable(objectMapper.writeValueAsString(o));
    }
}
