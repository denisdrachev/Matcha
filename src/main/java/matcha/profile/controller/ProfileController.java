package matcha.profile.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.profile.model.UserProfileWithoutEmail;
import matcha.profile.service.ProfileService;
import matcha.response.Response;
import matcha.userprofile.model.UserInfoModel;
import matcha.validator.ValidationMessageService;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ProfileController {

    private ValidationMessageService validationMessageService;
    private ProfileService profileService;

    @PostMapping(value = "profile-update1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response profileUpdate(@Valid @RequestBody UserInfoModel userProfile,
                                  BindingResult bindingResult,
                                  @CookieValue(value = "token") String token) {
        log.info("Request update user profile:{} token:{}", userProfile, token);
        if (bindingResult.hasErrors())
            return validationMessageService.prepareValidateMessage(bindingResult);

        profileService.saveUserInfoByToken(token, userProfile);
        return validationMessageService.prepareMessageOkOnlyType();
    }

    @GetMapping(value = "profile-get1/{login}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response getUserProfile(@PathVariable String login) {
        log.info("Request get user profile by login: {}", login);
        UserProfileWithoutEmail userProfile = profileService.getUserProfile(login);
        return validationMessageService.prepareMessageOkData(userProfile);
    }

}
