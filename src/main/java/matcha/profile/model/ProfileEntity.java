package matcha.profile.model;

import lombok.*;
import matcha.image.model.Image;
import matcha.userprofile.model.UserInfoModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEntity implements Serializable {

    private Integer id;
    private Integer age = null;
    private Integer gender = null;
    private List<Integer> preference = new ArrayList<>();
    private String biography;
    private List<String> tags = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
    private boolean isFilled = true;

    public ProfileEntity(int profileId, UserInfoModel userAndProfileUpdateModel) {
        this.id = profileId;
        this.age = userAndProfileUpdateModel.getAge();
        this.gender = userAndProfileUpdateModel.getGender();
        this.preference = userAndProfileUpdateModel.getPreference();
        this.biography = userAndProfileUpdateModel.getBiography();
        this.tags = userAndProfileUpdateModel.getTags();
        this.images = userAndProfileUpdateModel.getImages();
    }

    public String getPreferenceAsString() {
        String newPreference = null;
        newPreference = preference.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return newPreference;
    }

    public String getTagsAsString() {
        return String.join(",", tags);
    }
}
