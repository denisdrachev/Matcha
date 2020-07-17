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
    private Integer age;
    private Integer gender;
    private List<Integer> preference;
    private String biography;
    private List<String> tags;
    private List<ImageElem> images;
    private Integer avatar = -1;
    private Date time;

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
        time = user.getTime();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", login);
        jsonObject.put("fname", fname == null ? JSONObject.NULL : fname);
        jsonObject.put("lname", lname == null ? JSONObject.NULL : lname);
        jsonObject.put("email", email);
        jsonObject.put("age", age == null ? JSONObject.NULL : age);
        jsonObject.put("gender", gender == null ? JSONObject.NULL : gender);
        jsonObject.put("preference", preference);
        jsonObject.put("biography", biography == null ? JSONObject.NULL : biography);
        jsonObject.put("tags", tags);
        jsonObject.put("images", images);
        jsonObject.put("avatar", avatar == null ? JSONObject.NULL : avatar);
        jsonObject.put("time", time);
        return jsonObject;
    }

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }

}
