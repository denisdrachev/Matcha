package matcha.profile.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.image.manipulation.ImageManipulator;
import matcha.location.manipulation.LocationManipulator;
import matcha.model.Location;
import matcha.profile.db.ProfileDB;
import matcha.profile.model.ProfileModel;
import matcha.profile.model.UserProfileWithoutEmail;
import matcha.user.manipulation.UserManipulator;
import matcha.user.model.UserEntity;
import matcha.validator.ValidationMessageService;
import matcha.validator.ValidationService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileManipulator {

    private final ImageManipulator imageManipulator;
    private final UserManipulator userManipulator;
    private final LocationManipulator locationManipulator;

    private final ProfileDB profileDB;

    private final ValidationMessageService validationMessageService;
    private final ValidationService validationService;

    public UserProfileWithoutEmail getUserProfile(String login) {
        UserEntity user = userManipulator.getUserByLogin(login);
        Location activeUserLocation = locationManipulator.getLocationByUserIdAndActive(user.getId());
        user.setLocation(activeUserLocation);
        ProfileModel profileById = getProfileById(user.getProfileId());
        return new UserProfileWithoutEmail(user, profileById);
    }

    public ProfileModel getProfileById(int profileId) {
        return profileDB.getProfileById(profileId);
    }

    //TODO нужен ли этот метод?
//    public void profileUpdate(UserAndProfileUpdateModel userAndProfile) {
////        validationService.validateAvatarInImages(userAndProfile.getAvatar(), userAndProfile.getImages());
////        validationService.validateAvatarIndexInImages(userAndProfile.getAvatar(), userAndProfile.getImages());
//
//        userAndProfile.getLocation().setActive(true);
//        UserEntity userEntity = new UserEntity(userAndProfile);
//        userManipulator.userUpdate(userEntity);
//
//        ProfileModel profile = new ProfileModel(userAndProfile);
//        profile.setId(userEntity.getProfileId());
//
//        updateProfileByToken(profile);
//    }

    public void updateProfile(ProfileModel profile) {
        profileDB.updateProfileById(profile);
    }

    public Integer insertEmptyProfile() {
        return profileDB.insertEmptyProfile();
    }
}
