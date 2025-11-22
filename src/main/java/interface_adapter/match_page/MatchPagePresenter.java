package interface_adapter.match_page;

import use_case.update_matches.UpdateMatchesOutputBoundary;
import use_case.update_matches.UpdateMatchesOutputData;

public class MatchPagePresenter implements UpdateMatchesOutputBoundary {

    private final MatchPageViewModel matchPageViewModel;
    //    private final ViewManagerModel viewManagerModel;

    public MatchPagePresenter(MatchPageViewModel matchPageViewModel) {
        this.matchPageViewModel = matchPageViewModel;
        //        this.viewManagerModel = viewManagerModel; ViewManagerModel viewManagerModel,
    }

    @Override
    public void prepareSuccessView(UpdateMatchesOutputData response) {
        final MatchPageState matchPageState = matchPageViewModel.getState();
        matchPageState.setCurrentId(response.getUserProfile().getUserId());
        matchPageState.setEmail(response.getUserProfile().getEmail());
        matchPageState.setName(response.getUserProfile().getProfileUsername());
        matchPageState.setPhone(response.getUserProfile().getPhoneNumber());
        matchPageState.setResume(response.getUserProfile().getSummaryProfileAsString());
        // matchPageState.setUserMatches(response.);
        // matchPageState.setUserMatchesName(response.);
        this.matchPageViewModel.firePropertyChange();
    }
}
