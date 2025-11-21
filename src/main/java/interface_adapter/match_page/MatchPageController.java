package interface_adapter.match_page;

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
    public void execute(Integer currentId) {
        final UpdateMatchesInputData updateMatchesInputData = new UpdateMatchesInputData(currentId);
        updateMatchesUseCaseInteractor.execute(updateMatchesInputData);

    }
}
