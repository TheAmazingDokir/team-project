package use_case.change_profile_info;

import entity.UserProfile;

import java.util.List;

public interface ChangeProfileRecommendationAccess {
    /**
     * Adds profiles to recommendation service
     */
    void upsertProfiles(List<UserProfile> profiles);
}
