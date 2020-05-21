package matcha.mail;

import lombok.extern.slf4j.Slf4j;
import matcha.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Sender {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendRegistrationMail(String userEmail, String userActivationCode) {
        String subject = "Registration Confirmation";
        String confirmationUrl = "/regitrationConfirm.html?token=".concat(userActivationCode);
        String message = "hello manz";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message.concat("\r\n<a href=http://localhost:8888").concat(confirmationUrl).concat(">Активация</a>"));
        log.info("Sending mail: ".concat(email.toString()));
        mailSender.send(email);
        log.info("Message sent.");
        return true;
    }
}
