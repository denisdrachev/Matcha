package matcha.converter;

import matcha.model.OnlyAction;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class ConverterTest {

    @Test
    void convertToOnlyAction() {
        String s = "{\n" +
                "\"action\":\"aaaqqq\",\n" +
                "\t\"login\":\"ivan\",\n" +
                "\t\"password\":\"pass\",\n" +
                "\t\"fname\":\"first name\",\n" +
                "\t\"lname\":\"last name\",\n" +
                "\t\"email\":\"email@gmail.com\",\n" +
                "\t\"location\":{\n" +
                "\t\t\"x\":1,\n" +
                "\t\t\"y\":2\n" +
                "\t}\n" +
                "}";
        OnlyAction onlyAction = Converter.convertToOnlyAction(s);
        Assert.assertNotNull(onlyAction);
    }

    @Test
    void objectToJson() {
        Optional<String> s = Converter.objectToJson(null);
        Assert.assertTrue(s.isPresent());
        Assert.assertEquals(s.get(), "null");
    }
}