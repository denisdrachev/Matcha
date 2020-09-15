package matcha.profile.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.profile.service.ProfileService;
import matcha.response.Response;
import matcha.userprofile.model.UserAndProfileUpdateModel;
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
    public Response profileUpdate(@Valid @RequestBody UserAndProfileUpdateModel profileUpdateModel,
                                  @CookieValue(value = "token") String token,
                                  BindingResult bindingResult) {
        log.info("Income registration request. User:{} token:{}", profileUpdateModel, token);
        if (bindingResult.hasErrors())
            return validationMessageService.prepareValidateMessage(bindingResult);
        profileUpdateModel.setActivationCode(token);
        profileService.profileUpdate(profileUpdateModel);
        return validationMessageService.prepareMessageOkOnlyType();
    }

}
