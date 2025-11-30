package entity;

import java.util.List;

public class ProfileRecommendation {
    List<Integer> recommendedProfileIds;

    public ProfileRecommendation(List<Integer> recommendedProfileIds) {
        this.recommendedProfileIds = recommendedProfileIds;
    }

    public List<Integer> getRecommendedProfileIds() {
        return recommendedProfileIds;
    }

    public int getLastRecommendedProfileId(){
        return recommendedProfileIds.get(recommendedProfileIds.size()-1);
    }

    public int getLastRecommendedProfileId(List<Integer> seenProfileIds){
        for (int profileId : recommendedProfileIds){
            if(!seenProfileIds.contains(profileId)){
                return profileId;
            }
        }
        return getLastRecommendedProfileId();
    }
}
