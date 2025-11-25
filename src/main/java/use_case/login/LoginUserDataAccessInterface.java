package use_case.login;

import entity.User;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists.
     * @param name the username to look for
     * @param password the password to look for
     * @return true if a user with the given username exists with the given password set; false otherwise
     */
    boolean existsByNameAndPassword(String name, String password);
    int getUserIdByNameAndPassword(String name, String password);
}
