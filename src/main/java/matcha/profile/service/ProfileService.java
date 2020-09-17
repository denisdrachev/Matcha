package matcha.profile.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.image.service.ImageService;
import matcha.profile.manipulation.ProfileManipulator;
import matcha.profile.model.ProfileModel;
import matcha.profile.model.UserProfileWithoutEmail;
import matcha.user.manipulation.UserManipulator;
import matcha.user.model.UserEntity;
import matcha.user.service.UserService;
import matcha.userprofile.model.UserInfoModel;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileService {

    private ImageService imageService;
    private UserService userService;
    private ProfileManipulator profileManipulator;
    private UserManipulator userManipulator;
    private ValidationMessageService validationMessageService;

    public void saveProfile(ProfileModel profile) {
        profileManipulator.updateProfile(profile);
        imageService.saveImages(profile.getImages());
    }

    public void saveUserInfoByToken(String token, UserInfoModel userInfo) {

        UserEntity currentUser = userService.getUserByToken(token);
        UserEntity newUser = new UserEntity(userInfo);

        newUser.setId(currentUser.getId());
        newUser.setProfileId(currentUser.getProfileId());
        newUser.getLocation().setUser(currentUser.getId());
        newUser.setActive(currentUser.isActive());
        newUser.setBlocked(currentUser.isBlocked());

        ProfileModel newProfile = new ProfileModel(userInfo);
        newProfile.setId(currentUser.getProfileId());

        userService.saveUser(newUser);
        saveProfile(newProfile);
    }

//    //TODO точно ли первым двум строкам место в это методе или в контроллер их?
//    public void profileUpdate(UserAndProfileUpdateModel profile) {
//        userManipulator.checkUserActivationCode(token);
//        profileManipulator.profileUpdate(profile);
//    }

    public UserProfileWithoutEmail getUserProfile(String login) {
        return profileManipulator.getUserProfile(login);
    }

    public ProfileModel getProfileById(int id) {
        return profileManipulator.getProfileById(id);
    }

}
