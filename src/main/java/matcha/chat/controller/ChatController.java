package matcha.chat.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.chat.service.ChatService;
import matcha.response.Response;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping(value = "chat")
public class ChatController {

    private ChatService chatService;

    @PostMapping(value = "/{toLogin}/{fromLogin}", produces = "application/json")
    public Response postChatMessage(@PathVariable String toLogin, @PathVariable String fromLogin, @RequestBody String message) {
        log.info("Chat POST message to {} from {} message: {}", toLogin, fromLogin, message);
        return chatService.saveMessage(toLogin, fromLogin, message);
    }

    @Deprecated
    @GetMapping(value = "/{toLogin}/{fromLogin}/{limit}", produces = "application/json")
    public Response getMessagesByLimit(@PathVariable String toLogin, @PathVariable String fromLogin, @PathVariable int limit) {
        log.info("Chat GET message to {} from {} limit {}", toLogin, fromLogin, limit);
        return chatService.getMessages(toLogin, fromLogin, limit);
    }

    @GetMapping(value = "full/{toLogin}/{fromLogin}/{limit}", produces = "application/json")
    public Response getFullMessagesByLimit(@PathVariable String toLogin, @PathVariable String fromLogin, @PathVariable int limit) {
        log.info("Chat GET full message to {} from {} limit {}", toLogin, fromLogin, limit);
        return chatService.getFullMessages(toLogin, fromLogin, limit);
    }

    @GetMapping(value = "new/{toLogin}/{fromLogin}/{isRead}", produces = "application/json")
    public Response getNewMessages(@PathVariable String toLogin, @PathVariable String fromLogin, @PathVariable int isRead) {
        log.info("Chat GET new message to {} from {} isRead {}", toLogin, fromLogin, isRead);
        return chatService.getNewMessages(toLogin, fromLogin, isRead);
    }

    @GetMapping(value = "new/{toLogin}", produces = "application/json")
    public Response getNewMessages(@PathVariable String toLogin, @CookieValue(value = "token", defaultValue = "") String token) {
        if (token.isEmpty())
            System.err.println("TOKEN EMPTY");
        else
            System.err.println("TOKEN: " + token);
        log.info("Chat GET all new message to {}", toLogin);
        return chatService.getAllNewMessages(toLogin);
    }
}
