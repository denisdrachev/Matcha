package matcha.db;

import lombok.ToString;
import matcha.model.Location;
import matcha.model.User;
import org.junit.jupiter.api.Test;


import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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