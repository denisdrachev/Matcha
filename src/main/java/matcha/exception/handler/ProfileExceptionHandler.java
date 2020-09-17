package matcha.exception.handler;

import lombok.Data;
import matcha.exception.context.UserAlreadyExistException;
import matcha.exception.db.GetProfileByIdDBException;
import matcha.exception.db.location.GetActiveLocationByLoginException;
import matcha.exception.service.UserRegistryException;
import matcha.exception.user.UserAuthException;
import matcha.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ProfileExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(GetProfileByIdDBException.class)
//    protected ResponseEntity<AwesomeException> handleThereIsNoSuchUserException() {
//        return new ResponseEntity<>(new AwesomeException("Профиль не найден"), HttpStatus.OK);
//    }

    @ExceptionHandler({UserNotFoundException.class, UserAuthException.class, UserRegistryException.class,
            UserAlreadyExistException.class, GetProfileByIdDBException.class})
    protected ResponseEntity<AwesomeException> handleUserNotFoundException(Exception e) {
        return new ResponseEntity<>(new AwesomeException(e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(GetActiveLocationByLoginException.class)
    protected ResponseEntity<AwesomeException> handleBadNewsException() {
        return new ResponseEntity<>(new AwesomeException("Что-то явно пошло не по плану... Сообщите о случившемся кому-нибудь"), HttpStatus.OK);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    protected ResponseEntity<AwesomeException> handleCookieException() {
        return new ResponseEntity<>(new AwesomeException("Вы не авторизованы"), HttpStatus.OK);
    }

    @Data
    private static class AwesomeException {
        private String type = "error";
        private final String message;
    }
}
