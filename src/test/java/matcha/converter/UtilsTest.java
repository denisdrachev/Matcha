package matcha.converter;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

class UtilsTest {

    @Test
    void saveImageBase64ToFile() throws IOException {
        String filePath = "C:\\Users\\Ampersand\\IdeaProjects\\demo\\123.PNG";
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        System.err.println(encodedString);
//
//        String outputFileName = "C:\\Users\\Ampersand\\IdeaProjects\\demo\\111.PNG";
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
//        FileUtils.writeByteArrayToFile(new File(outputFileName), decodedBytes);

        String s = Utils.saveImageBase64ToFile(encodedString);
        System.err.println(s);
    }
}