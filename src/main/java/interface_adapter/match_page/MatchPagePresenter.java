package interface_adapter.match_page;

import entity.UserMatches;
import entity.UserProfile;
import use_case.update_matches.UpdateMatchesOutputBoundary;
import use_case.update_matches.UpdateMatchesOutputData;

import java.util.ArrayList;

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
        ArrayList<UserProfile> users = response.getUsers();
        matchPageState.setUserMatches(users);
        ArrayList<String> names = new ArrayList<>(users.size());
        for(int i= 0;i<users.size();i++){
            names.add(users.get(i).getProfileUsername());
        }
        matchPageState.setUserMatchesName(names);
        // matchPageState.setUserMatches(response.);
        // matchPageState.setUserMatchesName(response.);
        this.matchPageViewModel.firePropertyChange();
    }

    public void updateInfo(UpdateMatchesOutputData response) {
        final MatchPageState matchPageState = matchPageViewModel.getState();
        UserProfile user = null;
        for (UserProfile userProfile : matchPageState.getUserMatches()) {
            if (userProfile.getProfileUsername().equals(response.getCurrentName())) {
                user = userProfile;
            }
        }
        if (user != null) {
            matchPageState.setName(user.getProfileUsername());
            matchPageState.setEmail(user.getEmail());
            matchPageState.setPhone(user.getPhoneNumber());
            matchPageState.setResume(user.getFullProfileAsString());
            matchPageViewModel.firePropertyChange();
        }
    }
}
