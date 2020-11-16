package matcha.db;

import matcha.user.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.Date;

class EntityManipulatorTest {

    @Test
    void createUser() {
        UserEntity user = new UserEntity(0, "login", "password", "password".getBytes(),
                "activationCode", "fname", "lname", "email",
                false, false, new Date(), "aaad".getBytes(), 0, null);
    }

    @Test
    void updateProfileById() {

    }
}