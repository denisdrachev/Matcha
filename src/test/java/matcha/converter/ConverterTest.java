package matcha.converter;

import com.google.common.collect.Lists;
import matcha.model.ImageElem;
import matcha.model.OnlyAction;
import matcha.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;

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

    @Test
    void convertToImages() {
        String originalInput = "test input";
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        List<ImageElem> imageElems = Arrays.asList(
                new ImageElem(1, encodedString),
                new ImageElem(0, encodedString));
        String value1 = imageElems.toString();
        List<ImageElem> imageElems1 = Converter.convertToImages(value1);
        Assert.assertEquals(imageElems1, imageElems);
    }

    @Test
    void convertToUser() {
        String json = "{\n" +
                "\t\"login\":\"ivan3343s4\",\n" +
                "\t\"password\":\"test\",\n" +
                "\t\"fname\":\"first name\",\n" +
                "\t\"lname\":\"last name\",\n" +
                "\t\"email\":\"letasi6850@xenzld.com\",\n" +
                "\t\"location\":{\n" +
                "\t\t\"x\":1,\n" +
                "\t\t\"y\":2\n" +
                "\t}\n" +
                "}";
        User user = Converter.convertToUser(json);
        User u = new User();
        u.setPasswordBytes("test".getBytes());
        System.err.println(Arrays.toString(user.getPasswordBytes()));
        System.err.println(Arrays.toString(u.getPasswordBytes()));

        System.err.println(user.getPassword());
        System.err.println(u.getPassword());
    }
}