package matcha.db.crud;

public class Select {

    public static String selectImage = "SELECT id, index, src FROM images";
    public static String selectLocation = "SELECT * FROM locations";
    public static String selectRaiting = "SELECT * FROM rating";
    public static String selectBlacklist = "SELECT * FROM blacklist";
    public static String selectImageLikeEvent = "SELECT * FROM imageLikeEvents";
    public static String selectProfile = "SELECT * FROM profiles";
    public static String selectUsers = "SELECT * FROM users";

    public static String selectImagesCountById = "SELECT COUNT(*) FROM images WHERE id = ?";
    public static String selectLocationsCountById = "SELECT COUNT(*) FROM locations WHERE id = ?";
    public static String selectRatingCountById = "SELECT COUNT(*) FROM rating WHERE id = ?";
    public static String selectBlacklistCountById = "SELECT COUNT(*) FROM blacklist WHERE id = ?";
    public static String selectImageLikeEventsCountById = "SELECT COUNT(*) FROM imageLikeEvents WHERE id = ?";
    public static String selectProfilesCountById = "SELECT COUNT(*) FROM profiles WHERE id = ?";
    public static String selectUsersCountByLogin = "SELECT COUNT(*) FROM users WHERE login = ?";
    public static String selectUsersCountByActivationCode = "SELECT COUNT(*) FROM users WHERE activationCode = ?";
    public static String selectUsersCountByLoginAndActivationCode = "SELECT COUNT(*) FROM users WHERE login = ? AND activationCode = ?";
    public static String selectUserAndProfileByActivationCode = "SELECT * FROM users u INNER JOIN profiles p ON u.profileId = p.id WHERE activationCode = ?";

    public static String selectImageById = "SELECT * FROM images WHERE id = ? LIMIT 1";
    public static String selectLocationById = "SELECT * FROM locations WHERE id = ? LIMIT 1";
    public static String selectRatingById = "SELECT * FROM rating WHERE id = ? LIMIT 1";
    public static String selectBlacklistById = "SELECT * FROM blacklist WHERE id = ? LIMIT 1";
    public static String selectImageLikeEventById = "SELECT * FROM imageLikeEvents WHERE id = ? LIMIT 1";
    public static String selectProfileById = "SELECT * FROM profiles WHERE id = ? LIMIT 1";
    public static String selectUserByLogin = "SELECT * FROM users WHERE login = ? LIMIT 1";
    public static String selectUserById = "SELECT * FROM users WHERE id = ? LIMIT 1";
    public static String selectUserByActivationCode = "SELECT * FROM users WHERE activationCode = ? LIMIT 1";
}
