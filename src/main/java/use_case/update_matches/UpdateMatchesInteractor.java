package use_case.update_matches;

import entity.UserMatches;
import entity.UserProfile;
import data_access.MongoDBUserMatchesDataAccessObject;
import data_access.MongoDBProfileDataAccessObject;

import java.util.ArrayList;

/**
 * The Update Matches Interactor
 */
public class UpdateMatchesInteractor implements IUpdateMatchesInputBoundary {
    MongoDBUserMatchesDataAccessObject dao = new MongoDBUserMatchesDataAccessObject();

    /**
     * If both users have already approved each other, then add them to each others' matches attribute
     * @param user1Id the first user in the match
     * @param user2Id the second user in the match
     */
    public void createMatch(int user1Id, int user2Id) {
        UserMatches user1Object = dao.userIdtoUserMatches(user1Id);
        UserMatches user2Object = dao.userIdtoUserMatches(user2Id);

        if (user1Object.getApproved().contains(user2Id) &&  user2Object.getApproved().contains(user1Id)) {
            user1Object.addMatch(user2Id);
            user2Object.addMatch(user1Id);

            // update the User Matches in MongoDB
            dao.ChangeUserMatchesData(user1Object);
            dao.ChangeUserMatchesData(user2Object);
        }
    }

    public ArrayList<UserProfile> userIdtoUserMatchesProfiles(int userId) {
        // get the provided user's UserMatches object
        UserMatches userMatches = dao.userIdtoUserMatches(userId);

        // get the UserProfile objects of everyone this user has approved
        MongoDBProfileDataAccessObject profileDao = new MongoDBProfileDataAccessObject();
        ArrayList<UserProfile> approvedProfiles = new ArrayList<>();

        for (int approvedId : userMatches.getApproved()) {
            UserProfile approvedProfile = profileDao.getProfileById(approvedId);
            approvedProfiles.add(approvedProfile);
        }

        return approvedProfiles;
    }
}
