package interface_adapter.match_page;

import use_case.update_matches.*;

public class MatchPageController {

    private final UpdateMatchesInputBoundary updateMatchesUseCaseInteractor;

    public MatchPageController(UpdateMatchesInputBoundary updateMatchesUseCaseInteractor) {
        this.updateMatchesUseCaseInteractor = updateMatchesUseCaseInteractor;
    }

    /**
     * Executes the User Matches Use Case.
     * @param currentId user's ID
     */
    public void userIdtoUserMatchesProfiles(Integer currentId) {
        //final UpdateMatchesInputData updateMatchesInputData = new UpdateMatchesInputData(currentId);
        updateMatchesUseCaseInteractor.userIdtoUserMatchesProfiles(currentId);
    }
}
