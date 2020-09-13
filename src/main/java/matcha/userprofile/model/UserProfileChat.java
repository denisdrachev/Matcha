package matcha.userprofile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import matcha.chat.model.ChatMessage;
import matcha.model.ImageElem;
import matcha.response.Response;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileChat implements Response {

    private String login;
    private String fname;
    private String lname;
    private Integer gender;
    private List<String> tags;
    private List<ImageElem> images;
    private Integer avatar;
    private ChatMessage chatMessages;
}
