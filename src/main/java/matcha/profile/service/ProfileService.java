package matcha.profile.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.profile.manipulation.ProfileManipulator;
import matcha.profile.model.ProfileModel;
import matcha.response.Response;
import matcha.userprofile.model.UserAndProfileUpdateModel;
import matcha.validator.ValidationMessageService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileService {

    private ProfileManipulator profileManipulator;
    private ValidationMessageService validationMessageService;

    public Response profileSave(ProfileModel profile) {
        profileManipulator.profileSave(profile);
        return validationMessageService.prepareMessageOkOnlyType();
    }

    public void profileUpdate(UserAndProfileUpdateModel profile) {
        profileManipulator.profileUpdate(profile);
    }
}
