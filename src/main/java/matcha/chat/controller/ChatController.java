package matcha.chat.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.chat.component.ChatComponent;
import matcha.response.Response;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "chat")
public class ChatController {

    private ChatComponent chatComponent;

    @PostMapping(value = "/{toLogin}/{fromLogin}", produces = "application/json")
    public Response postChatMessage(@PathVariable String toLogin, @PathVariable String fromLogin, @RequestBody String message) {
        log.info("Chat POST message to {} from {} message: {}", toLogin, fromLogin, message);
        return chatComponent.saveMessage(toLogin, fromLogin, message);
    }

    @GetMapping(value = "/{toLogin}/{fromLogin}/{limit}", produces = "application/json")
    public Response getMessagesByLimit(@PathVariable String toLogin, @PathVariable String fromLogin, @PathVariable int limit) {
        log.info("Chat GET message to {} from {} limit {}", toLogin, fromLogin, limit);
        return chatComponent.getMessages(toLogin, fromLogin, limit);
    }

    @GetMapping(value = "full/{toLogin}/{fromLogin}/{limit}", produces = "application/json")
    public Response getFullMessagesByLimit(@PathVariable String toLogin, @PathVariable String fromLogin, @PathVariable int limit) {
        log.info("Chat GET full message to {} from {} limit {}", toLogin, fromLogin, limit);
        return chatComponent.getFullMessages(toLogin, fromLogin, limit);
    }

    @GetMapping(value = "/{toLogin}/{fromLogin}", produces = "application/json")
    public Response getNewMessages(@PathVariable String toLogin, @PathVariable String fromLogin) {
        log.info("Chat GET message to {} from {}", toLogin, fromLogin);
        return chatComponent.getNewMessages(toLogin, fromLogin);
    }

}
