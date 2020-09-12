package matcha.db.crud;

public class Update {
    public static String updateImageById = "UPDATE images set index = ?, src = ? WHERE id = ?";
    public static String updateLocationById = "UPDATE locations set active = ? WHERE id = ?";
    public static String updateRaitingById = "UPDATE rating set rating = ?, profile = ? WHERE id = ?";
    public static String updateBlacklistById = "UPDATE blacklist set who = ?, whom = ? WHERE id = ?";
    public static String updateImageLikeEventById = "UPDATE imageLikeEvents set active = ?, image = ?, who = ?, whom = ? WHERE id = ?";
    public static String updateProfileById = "UPDATE profiles set age = ?, gender = ?, preference = ?, biography = ?, tags = ?, images = ?, avatar = ? WHERE id = ?";
    public static String updateUserById = "UPDATE users set login = ?, activationCode = ?, fname = ?, lname = ?, email = ?, active = ?, blocked = ?, time = ?, profileId = ? WHERE id = ?";
    public static String updateUserByActivationCode = "UPDATE users set login = ?, fname = ?, lname = ?, email = ?, active = ?, blocked = ?, time = ? WHERE activationCode = ?";
    public static String updateChatMessage = "UPDATE chat set read = ? WHERE id = ?";
}
