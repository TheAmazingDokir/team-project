package use_case.recommend_profile;

import entity.EmployerProfile;
import entity.JobSeekerProfile;
import entity.ProfileRecommendation;
import entity.UserProfile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendProfileManualTest {

    @Test
    void testExecute_EmployerLookingForJobSeeker() {
        // ----------------------------------------------------------------
        // 1. Create a fake ProfileDAO manually
        // ----------------------------------------------------------------
        RecommendProfileProfileDataAccessInterface fakeProfileDao = new RecommendProfileProfileDataAccessInterface() {
            @Override
            public UserProfile getProfileById(int profileId) {
                // If querying ID 1, return the Employer (current user)
                if (profileId == 1) {
                    EmployerProfile employer = new EmployerProfile(1, "boss@test.com", "111", "Boss");
                    employer.setJobApplicationFull("We need a coder");
                    return employer;
                }
                // If querying ID 2, return the JobSeeker (recommended result)
                if (profileId == 2) {
                    return new JobSeekerProfile(2, "worker@test.com", "222", "Worker");
                }
                return null;
            }
        };

        // ----------------------------------------------------------------
        // 2. Create a fake Recommendation Service DAO manually
        // ----------------------------------------------------------------
        RecommendProfileRecommendationDataAccessInterface fakeRecDao = new RecommendProfileRecommendationDataAccessInterface() {
            @Override
            public ProfileRecommendation searchProfilesBySimilarity(String query, String role, int numberOfRecommendations) {
                // Simple validation to ensure correct role is passed
                if ("jobseeker".equals(role)) {
                    // Pretend the algorithm calculated ID 2 as the best match
                    List<Integer> ids = Arrays.asList(2);
                    return new ProfileRecommendation(ids);
                }
                return null;
            }
        };

        // ----------------------------------------------------------------
        // 3. Create a fake Matches DAO (seen records) manually
        // ----------------------------------------------------------------
        RecommendProfileMatchesDataAccessInterface fakeMatchesDao = new RecommendProfileMatchesDataAccessInterface() {
            @Override
            public List<Integer> getSeenProfileIds(int profileId) {
                // Pretend no one has been seen yet, return an empty list
                return new ArrayList<>();
            }
        };

        // ----------------------------------------------------------------
        // 4. Create a fake Presenter manually (to receive results)
        // ----------------------------------------------------------------
        // Create a variable to store the result for later verification
        final List<Integer> resultHolder = new ArrayList<>();

        RecommendProfileOutputBoundary fakePresenter = new RecommendProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(RecommendProfileOutputData outputData) {
                // Store the result
                resultHolder.add(outputData.getRecommendedProfile().getUserId());
            }
        };

        // ----------------------------------------------------------------
        // 5. Initialize Interactor and run
        // ----------------------------------------------------------------
        RecommendProfileInteractor interactor = new RecommendProfileInteractor(
                fakeProfileDao,
                fakeRecDao,
                fakePresenter,
                fakeMatchesDao
        );

        // Execute
        RecommendProfileInputData inputData = new RecommendProfileInputData(1);
        interactor.execute(inputData);

        // ----------------------------------------------------------------
        // 6. Verify the results
        // ----------------------------------------------------------------
        // Check if resultHolder contains exactly 1 result
        assertEquals(1, resultHolder.size());
        // Check if that result is ID 2
        assertEquals(2, resultHolder.get(0));
    }
}