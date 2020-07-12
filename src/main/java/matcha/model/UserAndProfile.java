package matcha.model;

import lombok.*;
import matcha.converter.Converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAndProfile implements Serializable {

    private String login;
    private String fname;
    private String lname;
    private String email;
    private int age;
    private int gender;
    private int preference;
    private String biography;
    private List<String> tags;
    private List<ImageElem> images = new ArrayList<>();
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

    @Override
    public String toString() {
        return Converter.objectToJson(this).get();
    }
}
