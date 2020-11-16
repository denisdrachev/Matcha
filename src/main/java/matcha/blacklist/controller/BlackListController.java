package matcha.blacklist.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.blacklist.model.BlackListMessage;
import matcha.blacklist.service.BlackListService;
import matcha.response.Response;
import matcha.validator.ValidationMessageService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping
public class BlackListController {

    private BlackListService blackListService;
    private ValidationMessageService validationMessageService;

    @PostMapping(value = "/blacklist/save", produces = "application/json")
    public Response saveBlackListMessage(
            @CookieValue(value = "token") String token,
            @RequestBody BlackListMessage message) {

        log.info("Request save chat message: {}", message);
        Response response = validationMessageService.validateMessage(message);
        if (response != null) {
            return response;
        }
        return blackListService.saveBlackList(token, message);
    }
}
