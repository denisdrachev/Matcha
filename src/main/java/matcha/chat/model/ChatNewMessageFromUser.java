package matcha.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatNewMessageFromUser {

    @NotBlank(message = "Поле 'toLogin' не может быть пустым")
    private String toLogin;
    @NotBlank(message = "Поле 'fromLogin' не может быть пустым")
    private String fromLogin;
    @NotNull
    private Integer isRead;
    private Date time = Calendar.getInstance().getTime();
}
