package matcha.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.profile.model.UserProfileWithoutEmail;
import matcha.response.Response;
import matcha.user.model.UserInfo;
import matcha.user.model.UserRegistry;
import matcha.user.service.UserService;
import matcha.validator.ValidationMessageService;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    private ValidationMessageService validationMessageService;
    private UserService userService;

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    //Errors errors,
    public Response registration(@RequestBody UserRegistry user) {

        log.info("Income registration request. User: {}", user);

        Response response = validationMessageService.validateMessage(user);
        if (response != null) {
            return response;
        }
        userService.userRegistration(user);
        return validationMessageService.prepareMessageOkOnlyType();
    }

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response login(@RequestBody UserInfo user) {
        log.info("Income registration request. User: {}", user);

        Response response = validationMessageService.validateMessage(user);
        if (response != null) {
            return response;
        }

        return userService.userLogin(user);
    }

    @GetMapping(value = "profile-get/{login}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response getUserProfile(@PathVariable String login) {
        log.info("Request get user profile by login: {}", login);
        UserProfileWithoutEmail userProfile = userService.getUserProfile(login);
        return validationMessageService.prepareMessageOkData(userProfile);
    }

    @GetMapping("/regitrationConfirm.html")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();
        boolean verificationToken = userService.activationUserByToken(token);

        if (!verificationToken) {
            String message = "auth.message.invalidToken"; //messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }
        return "redirect:?lang=" + request.getLocale().getLanguage();
    }
}
