package interface_adapter.match_page;

import use_case.approve_reject_profile.ApproveRejectProfileInputData;
import use_case.recommend_profile.RecommendProfileInputData;
import use_case.update_matches.*;

public class MatchPageController {

    private final UpdateMatchesInputBoundary updateMatchesUseCaseInteractor;

    public MatchPageController(UpdateMatchesInputBoundary updateMatchesUseCaseInteractor) {
        this.updateMatchesUseCaseInteractor = updateMatchesUseCaseInteractor;
    }

    /**
     * Executes the Home Screen Use Case.
     * @param
     */
    public void execute(Integer currentId, Integer otherId, boolean isApprove) {
        final UpdateMatchesInputData updateMatchesInputData =
                new ApproveRejectProfileInputData(currentId, otherId, isApprove);
        updateMatchesUseCaseInteractor.execute(approveRejectProfileInputData);

    }
}
