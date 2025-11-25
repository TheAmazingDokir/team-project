package use_case.signup;

import entity.User;

/**
 * DAO interface for the Signup Use Case.
 */
public interface SignupUserDataAccessInterface {

    /**
     * Checks if the given user exists.
     * @param name the username to look for
     * @param password the password to check for
     * @return true if a user with the given username with correct password exists; false otherwise
     */
    boolean existsByNameAndPassword(String  name, String password);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);

    /**
     * Retrieves next created user id from database
     * @return the not taken user id
     */
    int getNextUserId();
}
