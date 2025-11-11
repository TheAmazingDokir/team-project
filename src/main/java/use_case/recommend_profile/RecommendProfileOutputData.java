package use_case.recommend_profile;

import entity.UserProfile;

public class RecommendProfileOutputData {
    private UserProfile recommendedProfile;

    public RecommendProfileOutputData(UserProfile recommendedProfile) {
        this.recommendedProfile = recommendedProfile;
    }

    public UserProfile getRecommendedProfile() {
        return recommendedProfile;
    }
}
