package matcha.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageSave {

    @NotBlank(message = "Поле 'toLogin' не может быть пустым")
    private String toLogin;
    @NotBlank(message = "Поле 'fromLogin' не может быть пустым")
    private String fromLogin;
    @NotBlank(message = "Поле 'message' не может быть пустым")
    private String message;
    private Date time = Calendar.getInstance().getTime();
}
