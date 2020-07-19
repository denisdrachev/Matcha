package matcha.db.crud;

public class Insert {

    public static String insertImage = "INSERT INTO images (index, src) VALUES (?, ?)";
    public static String insertLocation = "INSERT INTO locations (user, x, y, time, active) VALUES (?, ?, ?, ?, ?)";
    public static String insertRaiting = "INSERT INTO rating (rating, profile) VALUES (?, ?)";
    public static String insertBlacklist = "INSERT INTO blacklist (who, whom) VALUES (?, ?)";
    public static String insertImageLikeEvent = "INSERT INTO imageLikeEvents (active, image, who, whom) VALUES (?, ?, ?, ?)";
    public static String insertProfile = "INSERT INTO profiles (age, gender, preference, biography, tags, images, avatar) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static String insertUser = "INSERT INTO users (login, password, activationCode, fname, lname, email, active, blocked, time, salt, profileId) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

}
