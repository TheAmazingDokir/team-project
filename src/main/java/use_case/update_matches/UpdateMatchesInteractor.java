package use_case.update_matches;

import entity.UserMatches;

/**
 * The Update Matches Interactor
 */
public class UpdateMatchesInteractor implements IUpdateMatchesInputBoundary {
    private final IUpdateMatchesUserDataAccess o = new UpdateMatchesUserDataAccessObject();

    /**
     * If both users have already approved each other, then add them to each others' matches attribute
     * @param user1Id the first user in the match
     * @param user2Id the second user in the match
     */
    public void execute(int user1Id, int user2Id) {
        UserMatches user1Object = o.getUserMatchesFromId(user1Id);
        UserMatches user2Object = o.getUserMatchesFromId(user2Id);

        if (user1Object.getApproved().contains(user2Id) &&  user2Object.getApproved().contains(user1Id)) {
            user1Object.addMatch(user2Id);
            user2Object.addMatch(user1Id);
        }
    }
}
