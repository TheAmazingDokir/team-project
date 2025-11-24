package use_case.update_matches;

/**
 * Input boundary for actions related to updating matches
 **/
public interface IUpdateMatchesInputBoundary {

    /**
     * Creates a match between the 2 users if they have already approved each other
     * @param user1Id the first user in the match
     * @param user2Id the second user in the match
     */
    void execute(int user1Id, int user2Id);
}
