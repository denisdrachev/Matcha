package matcha.converter;

import lombok.SneakyThrows;
import matcha.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

public class Utils {

    @SneakyThrows
    public static byte[] getPrepearPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
//        String s = Arrays.toString(hash);
//
//        String[] byteValues = s.substring(1, s.length() - 1).split(",");
//        byte[] bytes = new byte[byteValues.length];
//        for (int i=0, len=bytes.length; i<len; i++) {
//            bytes[i] = Byte.parseByte(byteValues[i].trim());
//        }
//        String str = new String(bytes);
        return hash;
    }

    public static boolean checkPassword(String inputPassword, byte[] salt, byte[] currentPassword) {
        byte[] prepearPassword = getPrepearPassword(inputPassword, salt);
        return Arrays.equals(currentPassword, prepearPassword);
    }



    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static User initRegistryUser(Object o, String password) {
        User user = (User) o;
        user.setActivationCode(UUID.randomUUID().toString());
        user.setSalt(Utils.getSalt());
        user.setPassword(Utils.getPrepearPassword(password, user.getSalt()));
        return user;
    }
}
