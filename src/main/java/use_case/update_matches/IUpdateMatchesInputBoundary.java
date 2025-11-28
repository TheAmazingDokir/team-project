package use_case.update_matches;

import entity.UserProfile;

import java.util.ArrayList;

/**
 * Input boundary for actions related to updating matches
 **/
public interface IUpdateMatchesInputBoundary {

    /**
     * Creates a match between the 2 users if they have already approved each other
     * @param
     */
    void createMatch(UpdateMatchesInputData updateMatchesInputData);

    /**
     * Finds all the people this user has approved, and returns their User Profile
     * @param
     */
    void userIdtoUserMatchesProfiles(UpdateMatchesInputData updateMatchesInputData);
}
