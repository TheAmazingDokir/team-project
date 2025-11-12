package use_case.recommend_profile;

public class RecommendProfileInputData {
    private int profileId;

    public RecommendProfileInputData(int profileId) {
        this.profileId = profileId;
    }

    public int getProfileId() {
        return profileId;
    }
}
