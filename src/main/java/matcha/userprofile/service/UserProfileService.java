package matcha.userprofile.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.db.chat.UserProfileManipulator;
import matcha.model.Profile;
import matcha.user.model.User;
import matcha.userprofile.model.UserProfileChat;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserProfileService implements UserProfileInterface {

    private UserProfileManipulator userProfileManipulator;

    @Override
    public UserProfileChat getChatUserProfile(String login) {
        try {
            User userByLogin = userProfileManipulator.getUserByLogin(login);
            Profile profileById = userProfileManipulator.getProfileById(userByLogin.getProfileId());
            return UserProfileChat.builder()
                    .login(userByLogin.getLogin())
                    .fname(userByLogin.getFname())
                    .lname(userByLogin.getLname())
                    .gender(profileById.getGender())
                    .tags(profileById.getTags())
                    .images(profileById.getImages())
                    .avatar(profileById.getAvatar())
                    .build();
        } catch (Exception e) {
            log.warn("Error get user profile for chat. User {} not found", login);
            return new UserProfileChat();
        }
    }
}
