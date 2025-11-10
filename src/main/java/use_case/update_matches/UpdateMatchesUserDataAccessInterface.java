package use_case.update_matches;

import entity.UserMatches;

/**
 * Contains necessary methods for the Update Matches use case
 */
public interface UpdateMatchesUserDataAccessInterface {

    /**
     * Gets the UserMatches object for the given User ID
     * @param userId User ID for the needed User object
     * @return UserMatches object for the give User ID
     */
    UserMatches getUserMatchesFromId(int userId);
}
