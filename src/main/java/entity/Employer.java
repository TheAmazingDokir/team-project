package entity;

/**
 * Represents an Employer in the system
 */
public class Employer extends User {

    /**
     * Creates a new Employer with the given name, password and UserProfile
     * @param name the username
     * @param password the password
     * @param profile the UserProfile object for this user
     */
    public Employer(String name, String password, UserProfile profile) {

        super(name, password, profile);
    }

    // Employer methods
}
