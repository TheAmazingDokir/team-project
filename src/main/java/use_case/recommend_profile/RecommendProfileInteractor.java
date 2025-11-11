package use_case.recommend_profile;

import entity.EmployerProfile;
import entity.UserProfile;
import entity.ProfileRecommendation;

import java.util.List;

public class RecommendProfileInteractor implements RecommendProfileInputBoundary {
    private final RecommendProfileProfileDataAccessInterface profileDataAccess;
    private final RecommendProfileRecommendationDataAccessInterface recomendationDataAccess;
    private final RecommendProfileMatchesDataAccessInterface matchesDataAccess;
    private final RecommendProfileOutputBoundary profilePresenter;

    public RecommendProfileInteractor(RecommendProfileProfileDataAccessInterface profileDataAccess, RecommendProfileRecommendationDataAccessInterface recomendationDataAccess, RecommendProfileOutputBoundary profilePresenter, RecommendProfileMatchesDataAccessInterface matchesDataAccess) {
        this.profileDataAccess = profileDataAccess;
        this.recomendationDataAccess = recomendationDataAccess;
        this.matchesDataAccess = matchesDataAccess;
        this.profilePresenter = profilePresenter;
    }

    @Override
    public void execute(RecommendProfileInputData recommendProfileInputData) {
        int profileId = recommendProfileInputData.getProfileId();
        UserProfile userProfile = profileDataAccess.getProfileById(profileId);

        List<Integer> seenProfileIds = matchesDataAccess.getSeenProfileIds(profileId);
        int numberOfRecordsToRequest = seenProfileIds.size() + 1;

        String query = userProfile.getSummaryProfileAsString();
        String role = userProfile instanceof EmployerProfile? "employer" : "jobseeker";
        ProfileRecommendation profileRecommendations = recomendationDataAccess.searchProfilesBySimilarity(query, role, numberOfRecordsToRequest);

        int recommendedId = profileRecommendations.getLastRecommendedProfileId();
        UserProfile recommendedProfile = profileDataAccess.getProfileById(recommendedId);

        RecommendProfileOutputData outputData = new  RecommendProfileOutputData(recommendedProfile);
        profilePresenter.prepareSuccessView(outputData);

    }

}
