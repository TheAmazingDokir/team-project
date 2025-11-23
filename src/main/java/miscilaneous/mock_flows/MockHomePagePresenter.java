package miscilaneous.mock_flows;

import use_case.recommend_profile.RecommendProfileOutputBoundary;
import use_case.recommend_profile.RecommendProfileOutputData;

public class MockHomePagePresenter implements RecommendProfileOutputBoundary {
    public void prepareSuccessView(RecommendProfileOutputData outputData){
        System.out.println(outputData.getRecommendedProfile().getProfileUsername());
        System.out.println(outputData.getRecommendedProfile().getFullProfileAsString());
    }
}
