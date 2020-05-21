package matcha.db.crud;

public class Update {
    public static String updateImageById = "UPDATE images set img = ? WHERE id = ?";
    public static String updateLocationById = "UPDATE locations set profile = ?, location = ? WHERE id = ?";
    public static String updateRaitingById = "UPDATE rating set rating = ?, profile = ? WHERE id = ?";
    public static String updateBlacklistById = "UPDATE blacklist set who = ?, whom = ? WHERE id = ?";
    public static String updateImageLikeEventById = "UPDATE imageLikeEvents set active = ?, image = ?, who = ?, whom = ? WHERE id = ?";
    public static String updateProfileById = "UPDATE profiles set age = ?, gender = ?, preference = ?, biography = ?, tags = ?, images = ?, avatar = ? WHERE id = ?";
    public static String updateUserById = "UPDATE users set activationCode = ?, active = ? WHERE id = ?";
}
