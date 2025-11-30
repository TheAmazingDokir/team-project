package interface_adapter.match_page;

import use_case.update_matches.*;

public class MatchPageController {

    private final IUpdateMatchesInputBoundary updateMatchesUseCaseInteractor;

    public MatchPageController(IUpdateMatchesInputBoundary updateMatchesUseCaseInteractor) {
        this.updateMatchesUseCaseInteractor = updateMatchesUseCaseInteractor;
    }

    /**
     * Executes the User Matches Use Case.
     * @param currentId user's ID
     */
    public void userIdtoUserMatchesProfiles(Integer currentId) {
        final UpdateMatchesInputData updateMatchesInputData = new UpdateMatchesInputData(currentId);
        updateMatchesUseCaseInteractor.userIdtoUserMatchesProfiles(updateMatchesInputData);
    }

    public void getInfo(String name){
        final UpdateMatchesInputData updateMatchesInputData = new UpdateMatchesInputData(name);
        updateMatchesUseCaseInteractor.getInfo(updateMatchesInputData);

    }

}
