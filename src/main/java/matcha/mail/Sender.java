package matcha.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.model.User;
import matcha.properties.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Sender {

    private JavaMailSender mailSender;
    private ConfigProperties configProperties;

    public boolean sendRegistrationMail(String userEmail, String userActivationCode) {
        if (!configProperties.isMailSend())
            return true;
        String subject = "Registration Confirmation";
        String confirmationUrl = "/regitrationConfirm.html?token=".concat(userActivationCode);
        String message = "hello manz";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
//        email.setText(message.concat("\r\n<a href=https://matcha-server.herokuapp.com").concat(confirmationUrl).concat(" target=\"_blank\">Активация</a>"));
        email.setText(message.concat("\r\n<a href=http://localhost:8888").concat(confirmationUrl).concat(" target=\"_blank\">Активация</a>"));

        log.info("Sending mail: ".concat(email.toString()));
        mailSender.send(email);
        log.info("Message sent.");
        return true;
    }
}
