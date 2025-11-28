package use_case.update_matches;

import entity.UserProfile;

import java.util.ArrayList;

/**
 * Input boundary for actions related to updating matches
 **/
public interface IUpdateMatchesInputBoundary {

    /**
     * Creates a match between the 2 users if they have already approved each other
     * @param user1Id the first user in the match
     * @param user2Id the second user in the match
     */
    void createMatch(int user1Id, int user2Id);

    /**
     * Finds all the people this user has approved, and returns their User Profile
     * @param userId the user whose approved profiles are desired
     * @return an ArrayList of the UserProfile objects of the users this user has approved
     */
    ArrayList<UserProfile>  userIdtoUserMatchesProfiles(int userId);
}
