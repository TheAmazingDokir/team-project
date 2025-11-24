package use_case.update_matches;

import entity.UserMatches;

/**
 * Contains implementations of necessary methods for the Update Matches use case
 */
public class UpdateMatchesUserDataAccessObject implements IUpdateMatchesUserDataAccess {
    /**
     * Gets the UserMatches object for the given User ID
     * @param userId User ID for the needed User object
     * @return UserMatches object for the give User ID
     */
    public UserMatches getUserMatchesFromId(int userId) {
        // update this to return userIDtoUserMatchesObject.get(userId)
        return null;
    }

}
