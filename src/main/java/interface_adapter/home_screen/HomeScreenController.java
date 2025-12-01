package interface_adapter.home_screen;


import use_case.approve_reject_profile.ApproveRejectProfileInputBoundary;
import use_case.approve_reject_profile.ApproveRejectProfileInputData;
import use_case.recommend_profile.RecommendProfileInputBoundary;
import use_case.recommend_profile.RecommendProfileInputData;
import use_case.recommend_profile.RecommendProfileInteractor;
import use_case.update_matches.IUpdateMatchesInputBoundary;
import use_case.update_matches.UpdateMatchesInputData;

public class HomeScreenController {

    private final ApproveRejectProfileInputBoundary approveRejectProfileUseCaseInteractor;
    private final RecommendProfileInputBoundary recommendationProfileUseCaseInteractor;
    private final IUpdateMatchesInputBoundary  updateMatchesUseCaseInteractor;

    public HomeScreenController(ApproveRejectProfileInputBoundary approveRejectProfileUseCaseInteractor,
                                RecommendProfileInteractor recommendationProfileUseCaseInteractor,
                                IUpdateMatchesInputBoundary updateMatchesUseCaseInteractor) {
        this.approveRejectProfileUseCaseInteractor = approveRejectProfileUseCaseInteractor;
        this.recommendationProfileUseCaseInteractor = recommendationProfileUseCaseInteractor;
        this.updateMatchesUseCaseInteractor = updateMatchesUseCaseInteractor;
    }


    /**
     * Executes the Home Screen Use Case.
     * @param currentId the id for the current object
     * @param otherId the id for the shown object
     * @param isApprove the Approve state between current and other
     */
    public void execute(Integer currentId, Integer otherId, boolean isApprove) {
        final ApproveRejectProfileInputData approveRejectProfileInputData =
                new ApproveRejectProfileInputData(currentId, otherId, isApprove);
        final UpdateMatchesInputData updateMatchesInputData = new UpdateMatchesInputData(currentId, otherId);
        final RecommendProfileInputData recommendationProfileInputData =
                new RecommendProfileInputData(currentId);

        approveRejectProfileUseCaseInteractor.execute(approveRejectProfileInputData);
        updateMatchesUseCaseInteractor.updateMatch(updateMatchesInputData);
        recommendationProfileUseCaseInteractor.execute(recommendationProfileInputData);

    }

    /**
     * Executes the Recommend Use Case separately (needed to get the initial recommendation)
     * @param currentId the id of current user
     */
    public void execute(Integer currentId) {
        final RecommendProfileInputData recommendationProfileInputData =
                new RecommendProfileInputData(currentId);
        recommendationProfileUseCaseInteractor.execute(recommendationProfileInputData);
    }
}


