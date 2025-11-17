package use_case.change_profile_info;

import entity.UserProfile;

/**
 * DAO interface for the ChangeProfile Use Case.
 */
public interface ChangeProfileDataAccessInterface {

    /**
     * Checks if the given UserProfile exists.
     * @param userID the profileID to look for
     * @return true if a user with the given profileID exists; false otherwise
     */
    boolean CheckProfileExists(Integer userID);
    /**
     * Updates the data of the given user.
     * @param userProfile the userProfile to look for
     * @return user object with the altered userProfile data.
     */
    void ChangeProfileData(UserProfile userProfile);
}
