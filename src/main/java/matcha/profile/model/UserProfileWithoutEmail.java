package matcha.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matcha.image.model.ImageModel;
import matcha.model.Location;
import matcha.user.model.UserEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileWithoutEmail implements Serializable {

    private String login;
    private String fname;
    private String lname;
    private Integer age;
    private Integer gender;
    private List<Integer> preference;
    private String biography;
    private List<String> tags;
    private List<ImageModel> images;
    private Integer avatar = -1;
    private Date time;
    private Location location;

    public UserProfileWithoutEmail(UserEntity user, ProfileModel profile) {
        login = user.getLogin();
        fname = user.getFname();
        lname = user.getLname();
        age = profile.getAge();
        gender = profile.getGender();
        preference = profile.getPreference();
        biography = profile.getBiography();
        tags = profile.getTags();
        images = profile.getImages();
        avatar = profile.getAvatar();
        time = user.getTime();
        location = user.getLocation();
    }
}
