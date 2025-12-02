package interface_adapter.create_profile;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
import interface_adapter.home_screen.HomeScreenState;
import interface_adapter.home_screen.HomeScreenViewModel;
import interface_adapter.login.LoginState;
import use_case.change_profile_info.ChangeProfileOutputBoundary;
import use_case.change_profile_info.ChangeProfileOutputData;

public class ChangeProfilePresenter implements ChangeProfileOutputBoundary {

    private final ChangeProfileViewModel changeProfileViewModel;

    private final ViewManagerModel viewManagerModel;

    private final HomeScreenViewModel homeScreenViewModel;


    public ChangeProfilePresenter(ChangeProfileViewModel changeProfileViewModel, ViewManagerModel viewManagerModel, HomeScreenViewModel homeScreenViewModel) {
        this.changeProfileViewModel = changeProfileViewModel;
        this.viewManagerModel = viewManagerModel;
        this.homeScreenViewModel = homeScreenViewModel;
    }

    @Override
    public void prepareSuccessView(ChangeProfileOutputData changeProfileOutputData) {

        changeProfileViewModel.setState(new ChangeProfileState());

        // switch to logged in view
        ViewManagerState state = this.viewManagerModel.getState();

        state.setLoggedIn(true);
        state.setHasProfile(true);
        state.setUserId(changeProfileOutputData.getUserID());

        HomeScreenState homeScreenState = homeScreenViewModel.getState();
        homeScreenState.setCurrentId(changeProfileOutputData.getUserID());
        homeScreenViewModel.setState(homeScreenState);
        homeScreenViewModel.firePropertyChange();
        state.setCurrentView(homeScreenViewModel.getViewName());

        this.viewManagerModel.setState(state);
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.out.println("ChangeProfilePresenter prepareFailView");
        ChangeProfileState state = changeProfileViewModel.getState();
        state.setErrorMessage(errorMessage);
        changeProfileViewModel.setState(state);
        changeProfileViewModel.firePropertyChange();
    }
}
