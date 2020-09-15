package matcha.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import matcha.image.model.ImageModel;
import matcha.model.Location;
import matcha.model.OnlyAction;
import matcha.model.UserAndProfile;
import matcha.user.model.UserEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;

class ConverterTest {

    @Test
    void convertToOnlyAction() {
        String s = "{\n" +
                "\"action\":\"aaaqqq\",\n" +
                "\t\"login\":\"ivan\",\n" +
                "\t\"password\":\"pass\",\n" +
                "\t\"fname\":\"first name\",\n" +
                "\t\"lname\":\"last name\",\n" +
                "\t\"email\":\"email@gmail.com\",\n" +
                "\t\"location\":{\n" +
                "\t\t\"x\":1,\n" +
                "\t\t\"y\":2\n" +
                "\t}\n" +
                "}";
        OnlyAction onlyAction = Converter.convertToOnlyAction(s);
        Assert.assertNotNull(onlyAction);
    }

    @Test
    void objectToJson() {
        Optional<String> s = Converter.objectToJson(null);
        Assert.assertTrue(s.isPresent());
        Assert.assertEquals(s.get(), "null");
    }

    @Test
    void convertToImages() {
        String originalInput = "test input";
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        List<ImageModel> imageElems = Arrays.asList(
                new ImageModel(1, encodedString),
                new ImageModel(0, encodedString));
        String value1 = imageElems.toString();
        List<ImageModel> imageElems1 = Converter.convertToImages(value1);
        Assert.assertEquals(imageElems1, imageElems);
    }

    @Test
    void convertToUser() {
        String json = "{\n" +
                "\t\"login\":\"ivan3343s4\",\n" +
                "\t\"password\":\"test\",\n" +
                "\t\"fname\":\"first name\",\n" +
                "\t\"lname\":\"last name\",\n" +
                "\t\"email\":\"letasi6850@xenzld.com\",\n" +
                "\t\"location\":{\n" +
                "\t\t\"x\":1,\n" +
                "\t\t\"y\":2\n" +
                "\t}\n" +
                "}";
        UserEntity user = Converter.convertToUser(json);
        UserEntity u = new UserEntity();
        u.setPasswordBytes("test".getBytes());
        System.err.println(Arrays.toString(user.getPasswordBytes()));
        System.err.println(Arrays.toString(u.getPasswordBytes()));

        System.err.println(user.getPassword());
        System.err.println(u.getPassword());
    }

    @Test
    void testConvertUserEntityToString() {
        int id = 1;
        String login = "login";
        String password = "password";
        byte[] passwordBytes = "passwordBytes".getBytes();
        String activationCode = "activationCode";
        String fname = "fname";
        String lname = "lname";
        String email = "email";
        boolean active = true;
        boolean blocked = true;
        Date time = Calendar.getInstance().getTime();
        byte[] salt = "salt".getBytes();
        Integer profileId = 14;
//        Location location = new Location(3, 5, 1.1, 1.2, true, Calendar.getInstance().getTime());
        Location location = null;
        UserEntity userEntity = new UserEntity(id, login, password, passwordBytes,
                activationCode, fname, lname, email, active, blocked, time, salt, profileId, location);

        String expectedStr = String.format("%s(id=%d, login=%s, fname=%s, lname=%s, email=%s, active=%b, blocked=%b, time=" + time + ", profileId=%d, location=%s)",
                UserEntity.class.getSimpleName(), id, login, fname, lname, email, active, blocked, profileId, location);
        Assert.assertEquals(expectedStr, userEntity.toString());
    }

    @Test
    void testObjectToJson() throws JSONException, JsonProcessingException {

        UserAndProfile userAndProfile = new UserAndProfile();
        userAndProfile.setLogin("login!");
        userAndProfile.setLname("lname!");
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject.put("string", "value");
        jsonObject.put("integer", 199);
        System.err.println(jsonObject);

        jsonObject2.put("user", userAndProfile);
        jsonObject2.put("ara", jsonObject.toString());
        System.err.println(jsonObject2);

        Map<String, Object> map = new HashMap<>();
        map.put("String", "value");
        map.put("user", userAndProfile);
        map.put("integer", 983);
        System.err.println(map);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("String", "value");
        hashMap.put("user", userAndProfile);
        hashMap.put("integer", 983);
        System.err.println(hashMap);
    }
}