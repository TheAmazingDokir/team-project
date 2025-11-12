package use_case.recommend_profile;

import entity.ProfileRecommendation;

public interface RecommendProfileRecommendationDataAccessInterface {
    public ProfileRecommendation searchProfilesBySimilarity(String query, String role, int numberOfRecommendations);
}
