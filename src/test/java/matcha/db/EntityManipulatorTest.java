package matcha.db;

import matcha.model.User;
import org.junit.jupiter.api.Test;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EntityManipulatorTest {

    @Test
    void createUser() {
        User user = new User(0,"111","222".getBytes(),"333",
                "444","555","666",false,false, new Date(),"aaad".getBytes(),0);

    }
}