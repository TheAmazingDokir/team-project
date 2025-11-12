package use_case.recommend_profile;

import entity.UserProfile;

public interface RecommendProfileProfileDataAccessInterface {
    public UserProfile getProfileById(int profileId);
}
