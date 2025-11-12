package entity;

/**
 * Represents an Employer in the system
 */
public class JobSeeker extends User {
    /**
     * Creates a new JobSeeker with the given name, password and UserProfile
     * @param name the username
     * @param password the password
     * @param profile the UserProfile object for this user
     */
    public JobSeeker(String name, String password, UserProfile profile) {

        super(name, password, profile);
    }

    // JobSeeker methods
}
