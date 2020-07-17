package matcha.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import matcha.converter.Converter;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAndProfile implements Serializable {

//    private User user;
//    private Profile profile;

    private String login;
    private String fname;
    private String lname;
    private String email;
    private int age;
    private int gender;
    private List<Integer> preference;
    private String biography;
    private List<String> tags;
    private List<ImageElem> images;
    private int avatar = -1;

    public UserAndProfile(User user, Profile profile) {
        login = user.getLogin();
        fname = user.getFname();
        lname = user.getLname();
        email = user.getEmail();
        age = profile.getAge();
        gender = profile.getGender();
        preference = profile.getPreference();
        biography = profile.getBiography();
        tags = profile.getTags();
        images = profile.getImages();
        avatar = profile.getAvatar();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", login);
        jsonObject.put("fname", fname);
        jsonObject.put("lname", lname);
        jsonObject.put("email", email);
        jsonObject.put("age", age);
        jsonObject.put("gender", gender);
        jsonObject.put("preference", preference);
        jsonObject.put("biography", biography);
        jsonObject.put("tags", tags);
        jsonObject.put("images", images);
        jsonObject.put("avatar", avatar);
        return jsonObject;
    }

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }

//    public ObjectNode toObjectNode() {
//        ObjectMapper ob = new ObjectMapper();
//        String s = Converter.objectToJson(this).get();
//        Map map = ob.readValue(s, Map.class);
//        return
//    }
}
