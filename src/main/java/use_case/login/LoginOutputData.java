package use_case.login;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final int userId;
    private final String username;
    private final boolean hasProfile;

    public LoginOutputData(int userId, String username, boolean hasProfile) {
        this.userId = userId;
        this.username = username;
        this.hasProfile = hasProfile;
    }

    public int getUserId() {
        return userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean hasProfile() {
        return hasProfile;
    }

}
