package matcha.db;

import matcha.user.model.User;
import org.junit.jupiter.api.Test;

import java.util.Date;

class EntityManipulatorTest {

    @Test
    void createUser() {
        User user = new User(0, "login", "password", "password".getBytes(),
                "activationCode","fname","lname","email",
                false,false, new Date(),"aaad".getBytes(),0, null);
    }

    @Test
    void updateProfileById() {

    }
}