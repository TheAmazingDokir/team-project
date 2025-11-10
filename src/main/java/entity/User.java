package entity;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {

    private String name;
    private String password;
    private UserProfile profile;

    /**
     * Creates a new user with the given non-empty name, non-empty password and UserProfile.
     * @param name the username
     * @param password the password
     * @param profile the UserProfile object for this user
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password, UserProfile profile) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserProfile getProfile(){
        return this.profile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfile(UserProfile profile){
        this.profile = profile;
    }

}
