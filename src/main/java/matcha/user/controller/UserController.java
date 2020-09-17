package matcha.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.response.Response;
import matcha.user.model.UserInfo;
import matcha.user.model.UserRegistry;
import matcha.user.service.UserService;
import matcha.validator.ValidationMessageService;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    private ValidationMessageService validationMessageService;
    private UserService userService;

    @PostMapping(value = "register1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response registration(@Valid @RequestBody UserRegistry user, BindingResult bindingResult) {
        log.info("Income registration request. User: {}", user);
        return bindingResult.hasErrors()
                ? validationMessageService.prepareValidateMessage(bindingResult)
                : userService.userRegistration(user);
    }

    @PostMapping(value = "login1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response login(@Valid @RequestBody UserInfo user, BindingResult bindingResult) {
        log.info("Income registration request. User: {}", user);
        return bindingResult.hasErrors()
                ? validationMessageService.prepareValidateMessage(bindingResult)
                : userService.userLogin(user);
    }
}
