package matcha.profile.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.profile.db.ProfileDB;
import matcha.profile.model.ProfileEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileManipulator {

    private final ProfileDB profileDB;

    public ProfileEntity getProfileById(int profileId) {
        return profileDB.getProfileById(profileId);
    }

    public void updateProfile(ProfileEntity profile) {
        profileDB.updateProfileById(profile);
    }

    public Integer createNewProfile() {
        return profileDB.insertEmptyProfile();
    }

    public void dropProfileById(int id) {
        profileDB.dropProfileById(id);
    }
}
