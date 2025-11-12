package use_case.recommend_profile;

import java.util.List;

public interface RecommendProfileMatchesDataAccessInterface {
    public List<Integer> getSeenProfileIds(int profileId);
}
