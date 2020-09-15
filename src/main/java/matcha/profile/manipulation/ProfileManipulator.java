package matcha.profile.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.image.manipulation.ImageManipulator;
import matcha.image.model.ImageModel;
import matcha.location.manipulation.LocationManipulator;
import matcha.model.UserAndProfile;
import matcha.profile.db.ProfileDB;
import matcha.profile.model.ProfileModel;
import matcha.response.Response;
import matcha.user.manipulation.UserManipulator;
import matcha.user.model.UserEntity;
import matcha.userprofile.model.UserAndProfileUpdateModel;
import matcha.validator.ValidationMessageService;
import matcha.validator.ValidationService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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

    //TODO мб не так что с методом, проверить
    public void profileSave(ProfileModel profile) {
        ProfileModel profileById = getProfileById(profile.getId());

        profileById.getImagesIds().forEach(imageManipulator::dropImageById);

        profile.getImages().forEach(imageElem -> {
            Integer integer = imageManipulator.insertImage(imageElem);
            imageElem.setId(integer);
        });

        profile.getImages().stream()
                .filter(imageElem -> imageElem.getIndex() == profile.getAvatar())
                .findFirst()
                .ifPresent(imageElem1 -> profile.setAvatar(imageElem1.getId()));

        updateProfileById(profile);
    }

    public Response profileGet(String login) {
        UserEntity user = userManipulator.getUserByLogin(login);
        user.setLocation(locationManipulator.getActiveLocationByLogin(user.getId()));
        try {
            ProfileModel profileById = getProfileById(user.getProfileId());

            UserAndProfile userAndProfile = new UserAndProfile(user, profileById);

            return validationMessageService.prepareMessageOkData(userAndProfile.toJSONObject());
        } catch (Exception e) {
            log.info("Error save profile. Message: {}", e.getMessage());
            return validationMessageService.prepareErrorMessage("Ошибка! Не удалось загрузить профиль!");
        }
    }

    public ProfileModel getProfileById(int profileId) {

        ProfileModel profileById = profileDB.getProfileById(profileId);
        if (profileById.getImagesIds() != null && profileById.getImagesIds().size() > 0) {
            profileById.getImagesIds().forEach(s -> {
                ImageModel imageById = imageManipulator.getImageById(s);
                profileById.getImages().add(imageById);
            });
        }
        return profileById;
    }


    //
//    //TODO куда его?! ПЛюс нужно подумать на ЮЗЕР_ПРОФИЛЬ пачки
    public void profileUpdate(UserAndProfileUpdateModel userAndProfile) {
        validationService.validateAvatarInImages(userAndProfile.getAvatar(), userAndProfile.getImages());
        validationService.validateAvatarIndexInImages(userAndProfile.getAvatar(), userAndProfile.getImages());

        userAndProfile.getLocation().setActive(true);
        UserEntity userEntity = new UserEntity(userAndProfile);
        userManipulator.userUpdate(userEntity);

        ProfileModel profile = new ProfileModel(userAndProfile);
        profile.setId(userEntity.getProfileId());

        profileSave(profile);
    }

    public void updateProfileById(ProfileModel profile) {
        ////TODO аватар доделать
        String imagesIds = profile.getImages().stream()
                .map(imageElem -> String.valueOf(imageElem.getId()))
                .collect(Collectors.joining(","));

        String preference = null;
        if (profile.getPreference().size() > 0)
            preference = profile.getPreference().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

        profileDB.updateProfileById(profile, preference, imagesIds);
    }
}
