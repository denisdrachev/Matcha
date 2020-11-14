package matcha.db.crud;

public class Update {
    public static String updateImageById = "UPDATE images set src = ?, avatar = ? WHERE id = ?";
    public static String updateLocationById = "UPDATE locations set active = ? WHERE id = ?";
    public static String updateLocationByLogin = "UPDATE locations l set l.active = ? WHERE l.user = (SELECT id FROM users WHERE login = ?)";
    public static String updateRaitingById = "UPDATE rating set rating = ?, profile = ? WHERE id = ?";
    public static String updateBlacklistById = "UPDATE blacklist set who = ?, whom = ? WHERE id = ?";
    public static String updateImageLikeEventById = "UPDATE imageLikeEvents set active = ?, image = ?, who = ?, whom = ? WHERE id = ?";
    public static String updateProfileById = "UPDATE profiles set age = ?, gender = ?, preference = ?, biography = ?, tags = ? WHERE id = ?";
    public static String updateUserById = "UPDATE users set login = ?, activationCode = ?, fname = ?, lname = ?, email = ?, active = ?, blocked = ?, time = ?, profileId = ? WHERE id = ?";
    public static String updateUserByLogin = "UPDATE users set fname = ?, lname = ?, email = ?, time = ? WHERE login = ?";
    public static String updateChatMessage = "UPDATE chat set read = ? WHERE id = ?";
}
