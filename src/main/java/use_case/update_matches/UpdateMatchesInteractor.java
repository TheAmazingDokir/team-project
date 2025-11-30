package use_case.update_matches;

import entity.UserMatches;
import entity.UserProfile;
import data_access.MongoDBUserMatchesDataAccessObject;
import data_access.MongoDBProfileDataAccessObject;
import use_case.recommend_profile.RecommendProfileOutputBoundary;

import java.util.ArrayList;

/**
 * The Update Matches Interactor
 */
public class UpdateMatchesInteractor implements IUpdateMatchesInputBoundary {
    MongoDBUserMatchesDataAccessObject dao = new MongoDBUserMatchesDataAccessObject();
    MongoDBProfileDataAccessObject profileDao = new MongoDBProfileDataAccessObject();
    private final UpdateMatchesOutputBoundary profilePresenter;

    public UpdateMatchesInteractor(UpdateMatchesOutputBoundary profilePresenter) {
        this.profilePresenter = profilePresenter;
    }

    public UpdateMatchesInteractor() {
        this.profilePresenter = null;
    }
    /**
     * If both users have already approved each other, then add them to each others' matches attribute
     * @param inputData
     */
    public void updateMatch(UpdateMatchesInputData inputData) {
        int user1Id = inputData.getUser1Id();
        int user2Id = inputData.getUser2Id();
        UserMatches user1Object = dao.getUserMatchesbyId(user1Id);
        UserMatches user2Object = dao.getUserMatchesbyId(user2Id);

        if (user1Object.getApproved().contains(user2Id) &&  user2Object.getApproved().contains(user1Id)) {
            user1Object.addMatch(user2Id);
            user2Object.addMatch(user1Id);

            // update the User Matches in MongoDB
            dao.changeUserMatchesData(user1Object);
            dao.changeUserMatchesData(user2Object);
        }
    }

    public void userIdtoUserMatchesProfiles(UpdateMatchesInputData inputData) {
        int userId = inputData.getCurrentId();
        // get the provided user's UserMatches object
        UserMatches userMatches = dao.getUserMatchesbyId(userId);

        // get the UserProfile objects of everyone this user has approved
        ArrayList<UserProfile> matchedProfiles = new ArrayList<>();

        for (int matchedId : userMatches.getMatches()) {
            UserProfile matchedProfile = profileDao.getProfileById(matchedId);
            matchedProfiles.add(matchedProfile);
        }
        UpdateMatchesOutputData outputData = new UpdateMatchesOutputData(matchedProfiles);

        profilePresenter.prepareSuccessView(outputData);

    }

    @Override
    public void getInfo(UpdateMatchesInputData updateMatchesInputData) {
        UpdateMatchesOutputData outputData = new UpdateMatchesOutputData(updateMatchesInputData.getCurrentName());
        profilePresenter.updateInfo(outputData);
    }
}
